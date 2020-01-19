package common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Ray
 * @create 2020-01-17 10:01:39
 * <p>包裹消息
 */
@Data
public class MessageWrapper {

    private Message message;
    private List<Server> servers = new ArrayList<>();

    public MessageWrapper(Message message,Server server){
        this.message = message;
        this.servers.add(server);
    }

    public MessageWrapper(Message message, Set<User> users){
        this.message = message;
        users.forEach(e->servers.add(e.getServer()));
    }
}
