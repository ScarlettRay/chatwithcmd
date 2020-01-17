package core;

import common.Server;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ray
 * @create 2019-12-02 13:42:04
 * <p>聊天室信息
 */
@Data
@Slf4j
public class ChatRoom {

    private Set<Server> servers = new HashSet<>();//聊天室内所有成员的位置;

    private Set<String> nickNames = new HashSet<>();//聊天室内所有成员的马甲

    private Server masterServer;

    private String myName;//我的入场id

    private ChatRoom(){}

    public static ChatRoom CHAT_ROOM = new ChatRoom();

    /**
     * 加入新的IP
     * @param server
     */
    public void addIp(Server server){
        this.servers.add(server);
    }

    public boolean addNickName(String nickName){
        if(nickNames.contains(nickName)){
            return false;
        }
        this.nickNames.add(nickName);
        return true;
    }

}
