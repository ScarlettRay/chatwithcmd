package net.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import core.Operation;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import common.Message;

/**
 * @author Ray
 * @create 2019-12-25 10:38:56
 * <p>消息处理Handler
 * 直接将消息打印在cmd上
 */
@Slf4j
public class DealMesRcvChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        //此方法并不会触发
        log.info("接收到一条新的消息:" + byteBuf.toString());
    }

    /**
     * 处理接收的消息，并将返回数据写入通道，返回给客户机
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        Message message = JSONObject.parseObject(msg.toString(), Message.class);
        Operation op = Operation.valueOf(message.getSignal().toString());
        Message re = op.deal(message);
        String reStr = JSON.toJSONString(re) + "\r\n";
        if(re != null){
            //回写数据
            ByteBuf buf = Unpooled.buffer();
            buf.writeBytes(reStr.getBytes());
            ctx.channel().writeAndFlush(buf);
        }
    }
}
