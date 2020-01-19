package core;

import common.Message;
import common.Server;
import common.User;
import net.MyChatClient;
import util.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    /**
     * 批量发送消息
     * @param servers
     * @param message
     * @return
     */
    public static Result batchSendRequired(List<Server> servers, Message message){
        for(Server tmpServer : servers){
            MyChatClient client = getClientFromPoolRequired(tmpServer);
            client.sendMessage(message);
        }
        return Result.OK;
    }

    /**
     * 根据User批量发送
     * @param users
     * @param message
     * @return
     */
    public static Result batchSendRequiredByUser(List<User> users, Message message){
        for (User user : users) {
            Server server = user.getServer();
            MyChatClient client = getClientFromPoolRequired(server);
            client.sendMessage(message);
        }
        return Result.OK;
    }

    /**
     * 向聊天室里的成员发送消息
     * @param message
     * @return
     */
    public static Result sendMessageInChatRoom(Message message){
        for (MyChatClient client : CLIENT_POOL) {
            client.sendMessage(message);
        }
        return Result.OK;
    }

}
