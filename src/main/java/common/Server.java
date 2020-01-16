package common;

import lombok.Data;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return port == server.port &&
                Objects.equals(ip, server.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, hostName, port);
    }
}
