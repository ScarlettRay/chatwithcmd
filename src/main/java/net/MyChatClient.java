package net;

import com.alibaba.fastjson.JSON;
import common.Server;
import net.channel.DealMesSendChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;
import util.Message;
import util.Result;

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

    public static final MyChatClient MYCHATCLIENT = new MyChatClient();

    private MyChatClient(){
        initlize();
    }
    /**
     * 初始化客户端
     */
    public void initlize(){
        // 首先，netty通过ServerBootstrap启动服务端
        client = new Bootstrap();

        //第1步 定义线程组，处理读写和链接事件，没有了accept事件
        group = new NioEventLoopGroup();
        client.group(group );

        //第2步 绑定客户端通道
        client.channel(NioSocketChannel.class);

        //第3步 给NIoSocketChannel初始化handler， 处理读写事件
        client.handler(new ChannelInitializer<NioSocketChannel>() {  //通道是NioSocketChannel
            @Override
            protected void initChannel(NioSocketChannel ch){
                //字符串编码器，一定要加在SimpleClientHandler 的上面
                ch.pipeline().addLast("http-codec", new HttpServerCodec());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(
                        Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                //找到他的管道 增加他的handler
                ch.pipeline().addLast(new DealMesSendChannelHandler());
            }
        });
    }

    public Result sendMessage(Message message, Server server){
        //连接服务器
        try {
            ChannelFuture future = client.connect(server.getIp(), server.getPort()).sync();
            future.channel().writeAndFlush(JSON.toJSON(message));
            //当通道关闭了，就继续往下走
            future.channel().closeFuture().sync();
            return Result.OK;
        } catch (InterruptedException e) {
            log.error("消息发送出现异常！"+ e.getMessage());
        }finally {
            group.shutdownGracefully();
        }
        return Result.ERROR;
    }

    /**
     * 批量发送
     * @param message
     * @param servers
     * @return
     */
    public Result batchSendMessage(Message message,List<Server> servers){
        for (Server e : servers) {
            Result result = sendMessage(message, e);
            if(result.isOK()){
                //TODO 成功或失败怎么处理
            }
        }
        return Result.OK;
    }

}
