package core;

import common.Server;
import common.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    private final static User currentUser = User.CURRENT_USER;//我的信息

    public final static ChatRoom CHAT_ROOM = new ChatRoom();

    private ChatRoom(){}

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
    public void removeUserByAdress(String adress){
        Iterator<User> its = users.iterator();
        while(its.hasNext()){
            User user = its.next();
            if(user.getServer().getIp().equals(adress)){
                its.remove();
                break;
            }
        }
    }

    /**
     * 去掉自己和指定user的聊天室内成员列表
     * @param user
     */
    public List<User> getUsersByCondition(User user){
        List<User> localUsers = new ArrayList<>(users);
        localUsers.remove(currentUser);
        localUsers.remove(user);
        return localUsers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setMasterServer(Server masterServer) {
        this.masterServer = masterServer;
    }
}
