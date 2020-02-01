import common.LatchContainer;
import common.Message;
import common.User;
import core.ChatRoom;
import core.ClientPool;
import core.CmdChatMachine;
import core.NewUserListener;
import lombok.extern.slf4j.Slf4j;
import net.MyChatServer;
import util.Constants;
import util.IOUtil;
import util.Result;
import util.Status;

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
        if(result.getStatusCode() == Status.NEW.getCode()){
            //新建聊天室
            ChatRoom.CHAT_ROOM.setMasterServer(User.CURRENT_USER.getServer());
            ChatRoom.CHAT_ROOM.addUser(User.CURRENT_USER);
            CmdChatMachine.CMD_CHAT_MACHINE.setMaster(true);
            log.info("已为你开通一个新的聊天室...");
            String input = IOUtil.input(Constants.NICK_NAME_TIP);
            User.CURRENT_USER.setUserName(input);
            //开启监听器
            new Thread(new NewUserListener()).start();
            log.info("等待其他成员的加入...");
        }else{
            try {
                LatchContainer.PREPARE_LATCH.await();
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
        while(true){
            String mes = IOUtil.input("");
            ClientPool.sendMessageInChatRoom(Message.buildUserMessage(mes));
        }
    }
}
