package util;

import common.Server;
import common.User;
import core.Signal;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * @author Ray
 * @create 2019-12-23 16:17:46
 * <p> 消息
 */
@Data
@Slf4j
public class Message {

    private Server server;
    private String message;
    private String userName; //聊天室的马甲
    private Status status;   //消息状态
    private Signal signal;   //消息类型，信号位



    public Message(String message,Signal signal){
        this.server = User.CURRENT_USER.getServer();
        this.message = message;
        this.signal = signal;
    }

    public Message(Status status,Signal signal){

    }

    /**
     * 构建用户消息
     * @return
     */
    public static Message buildUserMessage(String message){
        Message mes = new Message(message,Signal.MES);
        mes.setStatus(Status.OK);
        mes.setUserName(User.CURRENT_USER.getUserName());
        return mes;
    }

}
