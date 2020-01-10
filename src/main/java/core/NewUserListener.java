package core;

import com.alibaba.fastjson.JSON;
import common.User;
import lombok.extern.slf4j.Slf4j;
import net.MyChatClient;
import util.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author Ray
 * @create 2020-01-09 14:47:21
 * <p>新用户请求加入监听器
 */
@Slf4j
public class NewUserListener implements Runnable{

    private static final String BROADCAST_IP = "230.0.0.1";// 广播IP

    MulticastSocket broadSocket;// 用于接收广播信息
    InetAddress broadAddress;// 广播地址
    private static final int BROADCAST_INT_PORT = 10234; // 不同的port对应不同的socket发送端和接收端

    public NewUserListener(){
        // 初始化
        try {
            broadSocket = new MulticastSocket(BROADCAST_INT_PORT);
            broadAddress = InetAddress.getByName(BROADCAST_IP);

            broadSocket.joinGroup(broadAddress); // 加入到组播地址，这样就能接收到组播信息
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    public void run() {
        DatagramPacket inPacket;
        String messageStr;
        while (true) {
            try {
                inPacket = new DatagramPacket(new byte[1024], 1024);
                broadSocket.receive(inPacket); // 接收广播信息并将信息封装到inPacket中
                messageStr = new String(inPacket.getData(), 0,
                        inPacket.getLength());
                Message message = JSON.parseObject(messageStr,Message.class);

                if (message.getIp().equals(User.CURRENT_USER.getIp()))
                    continue; // 忽略自身
                if (message.getSignal().equals(Signal.DETECT)) { //是探测信息
                    Message re = Operation.DETECT.deal(message);
                    MyChatClient.MYCHATCLIENT.sendMessage(message,User.CURRENT_USER.getServer());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
