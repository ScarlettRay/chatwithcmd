package core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Message;
import util.Result;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author Administrator
 * @create 2019-12-23 15:15:48
 * <p>
 */
public class ChatRoomDetector {

    private static final Logger log = LoggerFactory.getLogger(CmdChatMachine.class);

    // 广播地址
    private static final String BROADCAST_IP = "230.0.0.1";// 广播IP

    private static final int BROADCAST_INT_PORT = 10234; // 不同的port对应不同的socket发送端和接收端

    private MulticastSocket broadSocket;// 用于接收广播信息

    private DatagramSocket detecter;//UDP聊天室探测器

    private InetAddress broadAddress;// 广播地址

    private Message helloMessage;

    public ChatRoomDetector(String ip,String hostName){
        try {
            detecter = new DatagramSocket();
            broadSocket = new MulticastSocket(BROADCAST_INT_PORT);
            broadAddress = InetAddress.getByName(BROADCAST_IP);
            helloMessage = new Message(ip,hostName,"Hello!I am son.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 探测当前局域网是否有聊天室
     * @return
     */
    public Result detect(){
        String message = JSONObject.toJSONString(helloMessage);
        byte[] mesByteArray = message.getBytes();
        DatagramPacket packet = new DatagramPacket(mesByteArray,mesByteArray.length, broadAddress,
                BROADCAST_INT_PORT); // 广播信息到指定端口
        try {
            detecter.send(packet);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Result.OK;
    }

}
