package core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @create 2019-12-02 13:42:04
 * <p>聊天室信息
 */
@Data
public class ChatRoom {

    private List<String> ips = new ArrayList<String>();//聊天室内所有成员的ip;

    private String masterIp;

    private String myName;//我的入场id


}
