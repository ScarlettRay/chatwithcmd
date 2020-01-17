package common;

import core.Signal;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import util.Status;


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

    private Signal signal;   //消息类型，信号位
    private String userName; //聊天室的马甲
    private Status status;   //消息状态



    public Message(String message,Signal signal){
        this.server = User.CURRENT_USER.getServer();
        this.message = message;
        this.signal = signal;
    }

    public Message(Status status,Signal signal){

    }

    public Message(Server server, String message, Signal signal, String userName, Status status) {
        this.server = server;
        this.message = message;
        this.signal = signal;
        this.userName = userName;
        this.status = status;
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

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Signal getSignal() {
        return signal;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }

}
