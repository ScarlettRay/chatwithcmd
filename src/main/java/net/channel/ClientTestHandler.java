package net.channel;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ray
 * @create 2020-01-13 16:23:01
 * <p>
 */
@Slf4j
public class ClientTestHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        log.info("发送消息：" + msg);
    }
}
