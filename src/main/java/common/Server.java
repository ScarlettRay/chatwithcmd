package common;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Administrator
 * @create 2020-01-02 17:42:04
 * <p>
 */
@Data
public class Server {

    private String ip;

    private String hostName;

    private int port;

    public Server(String ip,String hostName,int port){
        this.ip = ip;
        this.hostName = hostName;
        this.port = port;
    }

}
