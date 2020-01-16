import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Server;
import common.User;
import core.ClientPool;
import core.Signal;
import lombok.extern.slf4j.Slf4j;
import util.Message;
import util.Result;

/**
 * @author Ray
 * @create 2020-01-14 15:32:18
 * <p>客户端测试
 * 发送的消息一定要加上 \r\n
 */
@Slf4j
public class ClientTest {

    public static void main(String[] args) throws InterruptedException {
        Server server = User.CURRENT_USER.getServer();
        Message message = new Message("test!!", Signal.MES);
        Result re = ClientPool.getClientFromPoolRequired(User.CURRENT_USER.getServer()).sendMessage(message);
        log.info("消息状态：" + re.isOK());
    }
}
