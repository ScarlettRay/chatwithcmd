package net;

import com.alibaba.fastjson.JSON;
import common.Server;
import core.channel.DealMesSendChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.http.HttpServerCodec;
import util.Message;
import util.Result;

/**
 * @author Administrator
 * @create 2019-12-25 17:15:25
 * <p>聊天客户端，发送请求
 */
public class MyChatClient {

    public Result sendMessage(Message message, Server server){
        // 首先，netty通过ServerBootstrap启动服务端
        Bootstrap client = new Bootstrap();

        //第1步 定义线程组，处理读写和链接事件，没有了accept事件
        EventLoopGroup group = new NioEventLoopGroup();
        client.group(group );

        //第2步 绑定客户端通道
        client.channel(NioSocketChannel.class);

        //第3步 给NIoSocketChannel初始化handler， 处理读写事件
        client.handler(new ChannelInitializer<NioSocketChannel>() {  //通道是NioSocketChannel
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                //字符串编码器，一定要加在SimpleClientHandler 的上面
                ch.pipeline().addLast("http-codec", new HttpServerCodec());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(
                        Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                //找到他的管道 增加他的handler
                ch.pipeline().addLast(new DealMesSendChannelHandler());
            }
        });

        //连接服务器
        ChannelFuture future = null;
        try {
            future = client.connect(server.getIp(), server.getPort()).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        future.channel().writeAndFlush(JSON.toJSON(message));

        //当通道关闭了，就继续往下走
        try {
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
