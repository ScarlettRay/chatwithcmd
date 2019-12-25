package core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @create 2019-12-25 14:51:36
 * <p>
 */
public enum Operation implements DealRequest{

    /**
     * 处理探测之后返回的房间信息
     */
    JOIN("JOIN"){
        @Override
        public void deal(Message message){
            //JSONObject joinMes = JSON.parseObject(message.getMessage());
            ChatRoom chatRoom = JSON.parseObject(message.getMessage(),ChatRoom.class);
            synchronized (CmdChatMachine.CMD_CHAT_MACHINE){
                CmdChatMachine.CMD_CHAT_MACHINE.setChatRoom(chatRoom);
                CmdChatMachine.CMD_CHAT_MACHINE.setJoin(true);
                CmdChatMachine.CMD_CHAT_MACHINE.notify();
            }
            //返回请求加入聊天室的信息
        }
    },
    DETECT("DETECT"){
        @Override
        public void deal(Message message) {

        }
    };

    String OPERATION_TYPE;

    Operation(String operationType){
        this.OPERATION_TYPE = operationType;
    }
}
