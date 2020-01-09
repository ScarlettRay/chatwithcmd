package core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import util.MessageStatus;
import util.Result;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ray
 * @create 2019-12-02 14:18:54
 * <p>cmd聊天机器人，一个ip生成一个
 */
@Data
@Slf4j
public class CmdChatMachine implements ChatMachine{

    public static final CmdChatMachine CMD_CHAT_MACHINE = new CmdChatMachine();//一个进程只允许一个聊天机器

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private ChatRoomDetector detector;//UDP聊天室探测器

    private String ip;

    private String hostName;

    private boolean isMaster;//是否是聊天室admin

    private boolean isJoin;//是否已经加入聊天室

    private ChatRoom chatRoom;//当前聊天室信息

    private CmdChatMachine(){}

    /**
     * 初始化UDP聊天室探测器
     */
    private void initialize(){
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
            hostName = addr.getHostName();
            detector = new ChatRoomDetector(ip,hostName);
        } catch (UnknownHostException e){
            log.error(e.getMessage());
        }
    }

    /**
     * 加入当前局域网内的聊天室，如果有的话
     * @return
     */
    public Result attendChatRoom(){
        //探测局域网的聊天室
        Result re = detector.detect();
        if(re.isOK()){
            //发送成功,正在探测
            log.info("正在探测局域网内是否有聊天室...");
            //同步等待唤醒
            synchronized (CMD_CHAT_MACHINE){
                try {
                    CMD_CHAT_MACHINE.wait(5000);//等待五秒
                    if(!CMD_CHAT_MACHINE.isJoin){
                        log.info("正在进入聊天室...");
                        return Result.OK;
                    }else{
                        log.info("当前局域网没有聊天室...");
                        return Result.ERROR;
                    }
                } catch (InterruptedException e) {
                    log.error("attendChatRoom()抛出异常：" + e.getMessage());
                    return new Result(e);
                }
            }
        }
        return Result.ERROR;
    }

    public MessageStatus sendMessage(String message) {
        return null;
    }

    public MessagePackage ricvMessage() {
        return null;
    }

}

