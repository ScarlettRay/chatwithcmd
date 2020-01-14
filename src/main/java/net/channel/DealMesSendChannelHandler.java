package net.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @create 2020-01-02 17:29:37
 * <p>处理服务端返回的数据
 */
@Slf4j
public class DealMesSendChannelHandler  extends SimpleChannelInboundHandler<ByteBuf>{

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        log.info("接受消息:" + byteBuf.toString());
    }
}
