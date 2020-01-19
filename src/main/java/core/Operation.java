package core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.*;
import util.Constants;
import util.IOUtil;
import util.Status;

/**
 * @author Ray
 * @create 2019-12-25 14:51:36
 * <p>接受请求操作处理类
 */
public enum Operation {

    /**
     * 处理聊天室邀请消息
     */
    INVITE(Signal.INVITE){
        @Override
        public MessageWrapper deal(Message message){
            ChatRoom chatRoom = JSON.parseObject(message.getMessage(),ChatRoom.class);
            CmdChatMachine.CMD_CHAT_MACHINE.setChatRoom(chatRoom);
            CmdChatMachine.CMD_CHAT_MACHINE.setJoin(true);
            //输入聊天马甲
            String input = IOUtil.input(Constants.NICK_NAME_TIP);
            User.CURRENT_USER.setUserName(input);
            LatchContainer.INVITE_LATCH.countDown();//通知主线程继续执行
            return new MessageWrapper(new Message(Status.OK,Signal.ATTEND),chatRoom.getMasterServer());//将马甲提交给管理员校验，防止出现重名
        }
    },
    /**
     * 处理聊天室的探测消息
     * 返回当前聊天室的信息
     */
    DETECT(Signal.DETECT){
        @Override
        public MessageWrapper deal(Message message) {
            return new MessageWrapper(new Message(JSON.toJSONString(ChatRoom.CHAT_ROOM),Signal.INVITE),message.getServer());
        }
    },
    /**
     * 接受消息之后的返回接受成功信息
     */
    MES(Signal.MES){
        @Override
        public MessageWrapper deal(Message message) {
            IOUtil.output(message);
            return new MessageWrapper(new Message(Status.OK,Signal.SUC),message.getServer());
        }
    },
    /**
     * 对成功响应的消息不处理
     */
    SUC(Signal.SUC){
        @Override
        public MessageWrapper deal(Message message) {
            return null;
        }
    },
    /**
     * 加入聊天室的请求
     * 在聊天室Chatroom加入参与者的马甲和ip
     * 向新加入的参与者发送确认的马甲
     */
    ATTEND(Signal.ATTEND){
        @Override
        public MessageWrapper deal(Message message) {
            String userName = message.getUserName();
            int index = 2;
            //设置名称
            while (ChatRoom.CHAT_ROOM.hasNickName(userName)){
                userName += index;
            }
            //设置ip
            ChatRoom.CHAT_ROOM.addUser(new User(message.getServer(),userName));
            return new MessageWrapper(new Message(userName,Signal.ALLOW),message.getServer());
        }
    },
    /**
     * 允许加入的请求
     * 将返回来的信息加入本地的聊天室里边
     * 向管理员发送确认信息
     */
    ALLOW(Signal.ALLOW){
        @Override
        public MessageWrapper deal(Message message) {
            String nickName = message.getMessage();
            User.CURRENT_USER.setUserName(nickName);
            ChatRoom.CHAT_ROOM.addUser(User.CURRENT_USER);
            ChatRoom.CHAT_ROOM.setMasterServer(message.getServer());
            //允许主线程进入下一个输入消息阶段
            LatchContainer.PREPARE_LATCH.countDown();
            return new MessageWrapper(new Message(Status.OK,Signal.ACK),message.getServer());
        }
    },
    /**
     * 客户机答应加入聊天室，
     * 管理员需要给聊天室的所有人发新成员的信息
     */
    ACK(Signal.ACK){
        @Override
        public MessageWrapper deal(Message message) {
            User newUser = new User(message.getServer(),message.getUserName());
            Message mes = new Message(JSONObject.toJSONString(newUser),Signal.NEW);
            return new MessageWrapper(mes,ChatRoom.CHAT_ROOM.getUsers());
        }
    },
    /**
     * 新成员信息
     *
     */
    NEW(Signal.NEW){
        @Override
        public MessageWrapper deal(Message message) {
            User newUser = JSONObject.parseObject(message.getMessage(),User.class);
            ChatRoom.CHAT_ROOM.addUser(newUser);
            return new MessageWrapper(new Message(Status.OK,Signal.SUC),message.getServer());
        }
    },
    /**
     * 移除了用户之后的房间信息共享
     * 返回更新成功的信息
     */
    REMOVE(Signal.REMOVE){
        @Override
        public MessageWrapper deal(Message message){
            ChatRoom chatRoom = JSONObject.parseObject(message.getMessage(),ChatRoom.class);
            ChatRoom.CHAT_ROOM.setMasterServer(chatRoom.getMasterServer());
            ChatRoom.CHAT_ROOM.setUsers(chatRoom.getUsers());
            return new MessageWrapper(new Message(Status.OK,Signal.SUC),message.getServer());
        }
    };

    Signal OPERATION_TYPE;

    Operation(Signal operationType){
        this.OPERATION_TYPE = operationType;
    }

    public abstract MessageWrapper deal(Message message);

}
