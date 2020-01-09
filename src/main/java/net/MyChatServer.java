package net;

import net.channel.DealMesRcvChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Administrator
 * @create 2019-12-24 15:08:32
 * <p>
 *     端服务器
 */
@Slf4j
@Data
public class MyChatServer {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    /** 端口号 **/
    private int port;
    /** worker 线程数**/
    private int workerCount;  //如果是管理员，可以考虑加多线程数
    private int backlog = 1024;
    private boolean tcpNodelay = true;
    private boolean keepalive = true;

    public void startServer(){
        executorService.execute(()->{
                EventLoopGroup boss = new NioEventLoopGroup();
                EventLoopGroup worker = new NioEventLoopGroup(workerCount);
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(boss, worker);
                    bootstrap.channel(NioServerSocketChannel.class);
                    bootstrap.option(ChannelOption.SO_BACKLOG, backlog); //连接数
                    bootstrap.option(ChannelOption.TCP_NODELAY, tcpNodelay);  //不延迟，消息立即发送
//		            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000);  //超时时间
                    bootstrap.childOption(ChannelOption.SO_KEEPALIVE, keepalive); //长连接
                    bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel)
                                throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();

                            //HttpServerCodec: 针对http协议进行编解码
                            p.addLast("http-codec", new HttpServerCodec());
                            /**
                             * 作用是将一个Http的消息组装成一个完成的HttpRequest或者HttpResponse，那么具体的是什么
                             * 取决于是请求还是响应, 该Handler必须放在HttpServerCodec后的后面
                             */
                            p.addLast("aggregator", new HttpObjectAggregator(65536));
                            //ChunkedWriteHandler分块写处理，文件过大会将内存撑爆
                            p.addLast("http-chunked", new ChunkedWriteHandler());
                            //请求处理
                            p.addLast("dealMesHandle", new DealMesRcvChannelHandler());
                            //发送处理
                            //p.addLast("ackMesHandle", new AckMesChannelHandler());

                        }
                    });

                    ChannelFuture f = bootstrap.bind(port).sync();

                    if (f.isSuccess()) {
                        log.info("服务器启动成功...");
                    }
                    f.channel().closeFuture().sync();
                } catch (Exception e) {
                    log.error("启动服务器报错："+e.getMessage() );
                } finally {
                    boss.shutdownGracefully();
                    worker.shutdownGracefully();
                }
        });
    }

}
