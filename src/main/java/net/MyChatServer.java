package net;

import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import net.channel.DealMesRcvChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ray
 * @create 2019-12-24 15:08:32
 * <p>
 * 端服务器
 */
@Slf4j
@Data
public class MyChatServer {

    /**
     * 端口号
     **/
    private int port = 10234;
    /**
     * worker 线程数
     **/
    private int workerCount = 10;  //如果是管理员，可以考虑加多线程数
    private int backlog = 1024;
    private boolean tcpNodelay = true;
    private boolean keepalive = true;

    private ServerBootstrap bootstrap = null;
    private  EventLoopGroup boss = null;
    private EventLoopGroup worker = null;

    public void startServer() {
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker);
            bootstrap.option(ChannelOption.SO_BACKLOG, backlog); //连接数
            bootstrap.option(ChannelOption.TCP_NODELAY, tcpNodelay);  //不延迟，消息立即发送
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000);  //超时时间
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, keepalive); //长连接
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    ChannelPipeline p = socketChannel.pipeline();

                    p.addLast("string-decoder", new StringDecoder(StandardCharsets.UTF_8));
                    p.addLast("string-encoder", new StringEncoder(StandardCharsets.UTF_8));
                    socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));

                    //ChunkedWriteHandler分块写处理，文件过大会将内存撑爆
                    //p.addLast("http-chunked", new ChunkedWriteHandler());
                    //请求处理
                    p.addLast("dealMesHandle", new DealMesRcvChannelHandler());
                }
            });
            //同步，等待子线程任务返回
            CountDownLatch latch = new CountDownLatch(1);
            new Thread(new Sync(latch)).start();
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.interrupted();
            }

    }

    /**
     * 同步阶段，交给另一个线程去执行
     */
    private class Sync implements Runnable{

        CountDownLatch latch = null;

        Sync(CountDownLatch latch){
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                ChannelFuture f = bootstrap.bind(port).sync();
                if (f.isSuccess()) {
                    log.info("服务器启动成功...");
                }
                f.channel().closeFuture().sync();
            } catch (Exception e) {
                log.error("启动服务器报错：" + e.getMessage());
            } finally {
                log.info("server shut down!");
                boss.shutdownGracefully();
                worker.shutdownGracefully();
            }
        }
    }

}
