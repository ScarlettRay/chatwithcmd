package core;

import util.MessageStatus;
import util.Result;

/**
 * @author ray
 * @create 2019-12-02 13:40:35
 * <p>
 */
public interface ChatMachine {

    Result<ChatRoom> attendChatRoom();

    MessageStatus sendMessage(String message);

    MessagePackage ricvMessage();

}
