import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.Server;
import common.User;
import core.ClientPool;
import core.Signal;
import lombok.extern.slf4j.Slf4j;
import net.MyChatClient;
import util.Message;
import util.Result;

import java.util.Scanner;

/**
 * @author Ray
 * @create 2020-01-14 15:32:18
 * <p>客户端测试
 * 发送的消息一定要加上 \r\n
 */
@Slf4j
public class ClientTest {

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入聊天室马甲：");
        String msg = sc.next();
        User.CURRENT_USER.setUserName(msg);
        MyChatClient client = ClientPool.getClientFromPoolRequired(User.CURRENT_USER.getServer());
        while(){
            .sendMessage(Message.buildUserMessage(in))
        }
        while(sc.hasNext()){
            String in = sc.next();


            log.info("消息状态：" + re.isOK());
        }



    }
}
