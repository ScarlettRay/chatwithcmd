package net;

import com.alibaba.fastjson.JSON;
import common.Server;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import net.channel.ClientTestHandler;
import net.channel.DealMesSendChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import net.channel.test.SimpleClientHandler;
import util.Message;
import util.Result;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Administrator
 * @create 2019-12-25 17:15:25
 * <p>聊天客户端，发送请求
 */
@Slf4j
public class MyChatClient {

    private Bootstrap client = null;

    private EventLoopGroup group = null;
    private ChannelFuture future = null;

    private Server server = null;

    public static MyChatClient buildClient(Server server){
        //检查连接池中是否有指定server的客户端连接
        return new MyChatClient(server);
    }

    private MyChatClient(Server server){
        this.server = server;
        initlize(server);
    }
    /**
     * 初始化客户端
     */
    public void initlize(Server server){
        // 首先，netty通过ServerBootstrap启动服务端
        client = new Bootstrap();

        //第1步 定义线程组，处理读写和链接事件，没有了accept事件
        EventLoopGroup group = new NioEventLoopGroup();
        client.group(group );

        //第2步 绑定客户端通道
        client.channel(NioSocketChannel.class);

        //第3步 给NIoSocketChannel初始化handler， 处理读写事件
        client.handler(new ChannelInitializer<NioSocketChannel>() {  //通道是NioSocketChannel
            @Override
            protected void initChannel(NioSocketChannel ch){
                //字符串编码器，一定要加在SimpleClientHandler 的上面
                //ch.pipeline().addLast("http-codec", new HttpServerCodec());
                ch.pipeline().addLast("string-encoder",new StringEncoder(StandardCharsets.UTF_8));
                ch.pipeline().addLast("string-decoder",new StringDecoder(StandardCharsets.UTF_8));
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(
                        Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                //找到他的管道 增加他的handler
                ch.pipeline().addLast("deal-mes-send",new DealMesSendChannelHandler());
                ch.pipeline().addLast("client-test",new ClientTestHandler());
            }
        });

        try {
            future = client.connect(server.getIp(), server.getPort()).sync();
        } catch (InterruptedException e) {
            log.error("连接服务器出现异常！" + e.getMessage());
        }

    }

    public Result sendMessage(Message message){
        try {
            future.channel().writeAndFlush(JSON.toJSON(message) + "\r\n");
            //当通道关闭了，就继续往下走
            future.channel().closeFuture().sync();
            return Result.OK;
        } catch (InterruptedException e) {
            log.error("连接断开出现异常"+ e.getMessage());
        }finally {
            log.info("连接断开完成!");
            group.shutdownGracefully();
        }
        return Result.ERROR;
    }

    public Server getServer(){
        return server;
    }


}
