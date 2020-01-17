import common.User;
import core.Signal;
import lombok.extern.slf4j.Slf4j;
import net.MyChatClient;
import net.MyChatServer;
import common.Message;
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
        MyChatClient client = MyChatClient.buildClient(User.CURRENT_USER.getServer());
        Result re =client.sendMessage(message);
        log.info("消息状态：" + re.isOK());
    }
}
