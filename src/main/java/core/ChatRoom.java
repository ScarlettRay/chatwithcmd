package core;

import common.Server;
import common.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Ray
 * @create 2019-12-02 13:42:04
 * <p>聊天室信息
 */
@Data
@Slf4j
public class ChatRoom {

    private List<User> users = new ArrayList<>();//聊天室内所有成员

    private Server masterServer;

    private static final User currentUser = User.CURRENT_USER;//我的信息

    private ChatRoom(){}

    public static ChatRoom CHAT_ROOM = new ChatRoom();

    /**
     * 加入新的IP
     * @param user
     */
    public void addUser(User user){
        this.users.add(user);
    }

    public boolean hasNickName(String nickName){
        for(User tmpUser : users){
            if(nickName.equals(tmpUser.getUserName())){
               return true;
            }
        }
        return false;
    }

    /**
     * 通过ip删除房间里的用户
     * @return
     */
    public boolean removeUserByIp(String ip){
        Iterator<User> its = users.iterator();
        while(its.hasNext()){
            User user = its.next();
            if(user.getServer().getIp().equals(ip)){
                its.remove();
                return true;
            }
        }
        return false;
    }

}
