package net.channel;

import com.alibaba.fastjson.JSON;
import common.Message;
import common.User;
import core.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.CommonUtil;
import util.Status;

import java.net.InetSocketAddress;

/**
 * @author winray
 * @since v1.0.1
 */
public class RemoveClientChannelHandler extends ChannelInboundHandlerAdapter {

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
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String unconnectedAdress = ipSocket.getAddress().getHostAddress();
        if(ChatRoom.CHAT_ROOM.getMasterServer().getIp().equals(unconnectedAdress)){
            //是管理员，重新选举 TODO 选举结果还需要进行各个客户端之间的同步吗，还是直接依赖ChatRoom里面的user顺序，这样可靠吗
            ChatRoom.CHAT_ROOM.removeUserByAdress(unconnectedAdress);
            ClientPool.removeClientByAdress(unconnectedAdress);
            User master = ChatRoom.CHAT_ROOM.getUsers().get(0);//只要自己没退出，总会有一个，不用判断数组越界
            //判断是不是自己,还要开启新成员加入的监听器
            if(master.getServer().equals(User.CURRENT_USER.getServer())){
                CmdChatMachine.CMD_CHAT_MACHINE.setMaster(true);
                //开启监听器
                new Thread(new NewUserListener()).start();
            }
            ChatRoom.CHAT_ROOM.setMasterServer(master.getServer());
        }else{
            if(CmdChatMachine.CMD_CHAT_MACHINE.isMaster()){
                //移除用户
                CommonUtil.removeUserByAdress(unconnectedAdress);
                //向成员同步房间信息
                Message message = new Message(Status.OK, Signal.REMOVE);
                message.setMessage(JSON.toJSONString(ChatRoom.CHAT_ROOM));
                ClientPool.batchSendRequiredByUser(ChatRoom.CHAT_ROOM.getUsersByCondition(null),message);
            }else{
                //移除用户
                CommonUtil.removeUserByAdress(unconnectedAdress);
            }
        }
    }
}
