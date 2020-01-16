package core;

import common.Server;
import net.MyChatClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ray
 * @create 2020-01-16 10:26:42
 * <p>客户端连接池，
 * 防止出现重复建立客户端与服务端连接的情况
 * 当连接池中已存在相同的服务端连接时，捞出来直接用
 * TODO 这里需要花时间想一下怎么设计
 */
public class ClientPool {

    public static List<MyChatClient> CLIENT_POOL = new ArrayList<>();

    /**
     *从连接池中获取与指定服务器的连接，没有则返回null;
     * @param server
     * @return
     */
    public static MyChatClient getClientFromPool(Server server){
        for (MyChatClient e : CLIENT_POOL) {
            if (server.equals(e.getServer())) {
                return e;
            }
        }
        return null;
    }


    /**
     * 新建连接并添加到连接池中
     * @param server
     */
    public static MyChatClient addClient(Server server){
        if(getClientFromPool(server) == null){
            MyChatClient client = MyChatClient.buildClient(server);
            CLIENT_POOL.add(client);
            return client;
        }else{
            throw new RuntimeException("连接池中已存在相同的客户端与服务端的连接！");
        }
    }

    /**
     * 从连接池中获取与指定服务器的连接，没有则新建;
     * @param server
     * @return
     */
    public  static MyChatClient getClientFromPoolRequired(Server server){
        for (MyChatClient e : CLIENT_POOL) {
            if (server.equals(e.getServer())) {
                return e;
            }
        }
        return addClient(server);
    }

}
