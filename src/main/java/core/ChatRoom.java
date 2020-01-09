package core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ray
 * @create 2019-12-02 13:42:04
 * <p>聊天室信息
 */
@Data
@Slf4j
public class ChatRoom {

    private Set<String> ips = new HashSet<>();//聊天室内所有成员的ip;

    private Set<String> nickNames = new HashSet<>();//聊天室内所有成员的马甲

    private String masterIp;

    private String myName;//我的入场id

    private ChatRoom(){}

    public static ChatRoom CHAT_ROOM = new ChatRoom();

    /**
     * 加入新的IP
     * @param ip
     */
    public void addIp(String ip){
        this.ips.add(ip);
    }

    public boolean addNickName(String nickName){
        if(nickNames.contains(nickName)){
            return false;
        }
        this.nickNames.add(nickName);
        return true;
    }

}
