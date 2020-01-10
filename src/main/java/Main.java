import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import core.CmdChatMachine;
import core.NewUserListener;
import core.Signal;
import util.Message;

/**
 * @author Ray
 * @create 2019-12-02 14:20:35
 * <p>
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.printf("Hello hacker!");
        new Thread(new NewUserListener()).start();
        Thread.sleep(10000);
        CmdChatMachine.CMD_CHAT_MACHINE.attendChatRoom();
    }
}
