import core.ChatRoomDetector;
import core.ClientPool;
import core.CmdChatMachine;
import lombok.extern.slf4j.Slf4j;
import net.MyChatClient;
import net.MyChatServer;
import util.Message;

import java.util.Scanner;

/**
 * @author Ray
 * @create 2019-12-02 14:20:35
 * <p>主类
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws InterruptedException {
        //服务器启动
        MyChatServer server = new MyChatServer();
        server.startServer();
        //CmdChatMachine.CMD_CHAT_MACHINE.
        //局域网内广播是否存在聊天室
        ChatRoomDetector detector = CmdChatMachine.CMD_CHAT_MACHINE.getDetector();
        detector.detect();
        //等待输入
        Scanner sc = new Scanner(System.in);
        //批量发送给聊天室的所有人
        while(sc.hasNext()){
            String message = sc.next();
            for(MyChatClient client : ClientPool.CLIENT_POOL){
                client.sendMessage(Message.buildUserMessage(message));
            }
        }
    }
}
