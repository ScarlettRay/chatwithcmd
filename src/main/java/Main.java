import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import core.CmdChatMachine;
import core.Signal;
import util.Message;

/**
 * @author Administrator
 * @create 2019-12-02 14:20:35
 * <p>
 */
public class Main {

    public static void main(String[] args) {
        System.out.printf("Hello hacker!");
        CmdChatMachine.CMD_CHAT_MACHINE.attendChatRoom();
    }
}
