import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author winray
 * @since v1.0.1
 */
public class IteratorTest {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        Iterator<Integer> it = list.iterator();
        while(it.hasNext()){
            Integer tmp = it.next();
            it.remove();
            break;
        }
        for (Integer integer : list) {
            System.out.println(integer);
        }
    }
}
