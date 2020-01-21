import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @create 2019-12-09 14:30:41
 * <p>
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(getLocalBroadCast());
    }

    public static String getLocalBroadCast(){
        String broadCastIp = null;
        try {
            Enumeration<?> netInterfaces = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) netInterfaces.nextElement();
                if (!netInterface.isLoopback()&& netInterface.isUp()) {
                    List<InterfaceAddress> interfaceAddresses = netInterface.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                        //只有 IPv4 网络具有广播地址，因此对于 IPv6 网络将返回 null。
                        if(interfaceAddress.getBroadcast()!= null){
                            broadCastIp =interfaceAddress.getBroadcast().getHostAddress();

                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return broadCastIp;
    }
}
