package core;

import util.MessageStatus;
import util.Status;

/**
 * @author Ray
 * @create 2019-12-02 13:54:14
 * <p>
 */
public class MessagePackage {

    private MessageStatus messageStatus;

    private String message;

    private MessagePackage(String message, Status status){
        this.message = message;
        messageStatus = new MessageStatus(status);
    }
}
