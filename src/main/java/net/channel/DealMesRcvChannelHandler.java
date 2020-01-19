package net.channel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.MessageWrapper;
import common.User;
import core.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import common.Message;
import util.Status;

import java.net.InetSocketAddress;

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
        MessageWrapper re = op.deal(message);
        if(re == null)return;
        //有些消息是要返回给整个聊天室的成员的或者其他成员的，需要区分
        if(re.getServers().size() == 1 && re.getServers().get(0).equals(User.CURRENT_USER.getServer())){
            //直接回写
            if(re.getMessage() != null){
                String reStr = JSON.toJSONString(re.getMessage()) + "\r\n";
                //回写数据
                ByteBuf buf = Unpooled.buffer();
                buf.writeBytes(reStr.getBytes());
                ctx.channel().writeAndFlush(buf);
            }
        }else{
            //批量发送
            ClientPool.batchSendRequired(re.getServers(),re.getMessage());
        }
    }

    /**
     * 处理连接断开的情况
     * 分两种情况
     * 1.管理员
     * 2.非管理员
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        ChatRoom.CHAT_ROOM.getMasterServer();
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        if(ChatRoom.CHAT_ROOM.getMasterServer().getIp().equals(clientIp)){
            //是管理员，重新选举 TODO 选举结果还需要进行各个客户端之间的同步吗，还是直接依赖ChatRoom里面的user顺序，这样可靠吗
            ChatRoom.CHAT_ROOM.removeUserByIp(clientIp);
            User master = ChatRoom.CHAT_ROOM.getUsers().get(0);//只要自己没退出，总会有一个，不用判断数组越界
            //判断是不是自己
            if(master.getServer().equals(User.CURRENT_USER.getServer())){
                CmdChatMachine.CMD_CHAT_MACHINE.setMaster(true);
            }
            ChatRoom.CHAT_ROOM.setMasterServer(master.getServer());
        }else{
            if(CmdChatMachine.CMD_CHAT_MACHINE.isMaster()){
                //移除用户
                ChatRoom.CHAT_ROOM.removeUserByIp(clientIp);
                //向成员同步房间信息
                Message message = new Message(Status.OK, Signal.REMOVE);
                message.setMessage(JSON.toJSONString(ChatRoom.CHAT_ROOM));
                ClientPool.batchSendRequiredByUser(ChatRoom.CHAT_ROOM.getUsers(),message);
            }else{
                //移除用户
                ChatRoom.CHAT_ROOM.removeUserByIp(clientIp);
            }
        }
    }
}
