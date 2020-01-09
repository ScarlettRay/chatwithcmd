package core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.Message;

/**
 * @author Ray
 * @create 2019-12-25 14:51:36
 * <p>请求操作处理类
 */
public enum Operation {

    /**
     * 处理探聊天室邀请消息
     */
    INVITE(Signal.INVITE){
        @Override
        public void deal(Message message){
            ChatRoom chatRoom = JSON.parseObject(message.getMessage(),ChatRoom.class);
            synchronized (CmdChatMachine.CMD_CHAT_MACHINE){
                CmdChatMachine.CMD_CHAT_MACHINE.setChatRoom(chatRoom);
                CmdChatMachine.CMD_CHAT_MACHINE.setJoin(true);
                CmdChatMachine.CMD_CHAT_MACHINE.notify();
            }

        }
    },
    /**
     * 处理聊天室的探测消息
     */
    DETECT(Signal.DETECT){
        @Override
        public void deal(Message message) {

        }
    },
    /**
     * 接受消息之后的返回接受成功信息
     */
    RCV(Signal.RCV){
        @Override
        void deal(Message message) {

        }
    };

    Signal OPERATION_TYPE;

    Operation(Signal operationType){
        this.OPERATION_TYPE = operationType;
    }

    abstract void deal(Message message);

}
