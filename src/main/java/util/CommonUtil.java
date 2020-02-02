package util;

import core.ChatRoom;
import core.ClientPool;

/**
 * @author winray
 * @since v1.0.1
 *
 */
public class CommonUtil {

    public static void removeUserByAdress(String unconnectedAdress){
        ChatRoom.CHAT_ROOM.removeUserByAdress(unconnectedAdress);//移除用户
        ClientPool.removeClientByAdress(unconnectedAdress);//移除链接
    }
}
