package common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Ray
 * @create 2020-01-09 15:15:24
 * <p>用户，储存用户信息
 */
@Slf4j
@Data
public class User {
    //当前用户信息
    public static final User CURRENT_USER = new User();

    private Server server;
    private String userName; //聊天室的马甲

    private User(){}

    {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            server = new Server( addr.getHostAddress(),addr.getHostName(),10234);
        } catch (UnknownHostException e){
            log.error(e.getMessage());
        }
    }
}
