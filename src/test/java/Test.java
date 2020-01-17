import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @create 2019-12-09 14:30:41
 * <p>
 */
public class Test {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(5000, TimeUnit.MILLISECONDS);
            System.out.println("suc");
        } catch (InterruptedException e) {
            Thread.interrupted();
            System.out.println("fail");
        }
        System.out.println("finish!");
    }
}
