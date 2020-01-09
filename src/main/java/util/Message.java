package util;

import core.Signal;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Ray
 * @create 2019-12-23 16:17:46
 * <p> 消息
 */
@Data
@Slf4j
public class Message {

    private static String ip;
    private static String hostName;
    private String message;
    private static String userName; //聊天室的马甲
    private Status status;   //消息状态
    private Signal signal;   //消息类型，信号位

    static{
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
            hostName = addr.getHostName();
        } catch (UnknownHostException e){
            log.error(e.getMessage());
        }
    }

    public Message(String message,Signal signal){
        this.message = message;
        this.signal = signal;
    }

    public Message(Status status,Signal signal){

    }

    public static void setUserName(String userName){
        Message.userName = userName;
    }

    public String getUserName(){
        return userName;
    }
    public static String getIp(){
        return ip;
    }

}
