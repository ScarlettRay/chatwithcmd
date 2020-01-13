import common.User;
import core.Signal;
import net.MyChatClient;
import net.MyChatServer;
import util.Message;

/**
 * @author Ray
 * @create 2020-01-10 14:58:47
 * <p>服务器与客户端
 */
public class ServerAndClientTest {

    public static void main(String[] args) throws InterruptedException {
        MyChatServer server = new MyChatServer();
        server.startServer();
        Thread.sleep(5000);
        Message message = new Message("test!!", Signal.MES);
        MyChatClient.MYCHATCLIENT.sendMessage(message, User.CURRENT_USER.getServer());
    }
}
