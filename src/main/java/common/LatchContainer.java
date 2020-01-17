package common;

import java.util.concurrent.CountDownLatch;

/**
 * @author Ray
 * @create 2020-01-17 08:59:50
 * <p>闭锁容器
 */
public class LatchContainer {

    //等待邀请消息返回的闭锁
    public static final CountDownLatch INVITE_LATCH = new CountDownLatch(1);
}
