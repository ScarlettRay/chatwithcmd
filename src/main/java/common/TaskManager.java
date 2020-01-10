package common;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Ray
 * @create 2020-01-10 14:21:14
 * <p> 线程池管理
 */
public class TaskManager {

    //简单搞一个线程池
    public final static Executor THREAD_POOL = Executors.newFixedThreadPool(6);

}
