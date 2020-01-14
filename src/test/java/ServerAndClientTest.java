import common.User;
import core.Signal;
import lombok.extern.slf4j.Slf4j;
import net.MyChatClient;
import net.MyChatServer;
import util.Message;
import util.Result;

/**
 * @author Ray
 * @create 2020-01-10 14:58:47
 * <p>服务器与客户端
 */
@Slf4j
public class ServerAndClientTest {

    public static void main(String[] args) throws InterruptedException {
        MyChatServer server = new MyChatServer();
        server.startServer();
        Thread.sleep(5000);
        Message message = new Message("test!!", Signal.MES);
        Result re = MyChatClient.MYCHATCLIENT.sendMessage(message, User.CURRENT_USER.getServer());
        log.info("消息状态：" + re.isOK());
    }
}
