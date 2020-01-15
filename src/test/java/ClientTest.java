import common.User;
import core.Signal;
import lombok.extern.slf4j.Slf4j;
import net.MyChatClient;
import util.Message;
import util.Result;

/**
 * @author Ray
 * @create 2020-01-14 15:32:18
 * <p>客户端测试
 */
@Slf4j
public class ClientTest {

    public static void main(String[] args) {
        Message message = new Message("test!!", Signal.MES);
        Result re = MyChatClient.MYCHATCLIENT.sendMessage(message, User.CURRENT_USER.getServer());
        log.info("消息状态：" + re.isOK());
    }
}
