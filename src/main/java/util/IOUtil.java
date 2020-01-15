package util;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author Ray
 * @create 2020-01-09 08:58:52
 * <p>cmd输入工具
 */
@Slf4j
public class IOUtil {

    /**
     * 获取输入文字
     * @param tip 输入提示文字
     */
    public static String input(String tip){
        Scanner sc = new Scanner(System.in);
        String str = null;
        do{
            log.info(tip + ":");
            str = sc.next();
        }while (str == null || "".equals(str));
        return str;
    }

    /**
     * 输出消息
     * @param message
     */
    public static void output(Message message){
        log.info(message.getUserName() + ":" + message.getMessage());
    }
}