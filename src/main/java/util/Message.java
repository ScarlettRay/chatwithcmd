package util;

import lombok.Data;

/**
 * @author Administrator
 * @create 2019-12-23 16:17:46
 * <p> 消息
 */
@Data
public class Message {

    private String ip;
    private String hostName;
    private String message;
    private String userName; //聊天室的马甲
    private Status status;   //什么类型的消息

    public Message(String ip,String hostName,String message){
        this.ip = ip;
        this.hostName = hostName;
        this.message = message;
    }

    /**
     {ips:[ip1,ip2],m:ip3}
     */

}
