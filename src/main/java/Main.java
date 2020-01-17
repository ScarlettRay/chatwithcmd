import core.CmdChatMachine;
import lombok.extern.slf4j.Slf4j;
import net.MyChatServer;
import util.Result;

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
        Result result = CmdChatMachine.CMD_CHAT_MACHINE.attendChatRoom();

    }
}
