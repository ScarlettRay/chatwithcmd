package util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

class LanSend {

    // 广播地址
    private static final String BROADCAST_IP = "230.0.0.1";// 广播IP
    private static final int BROADCAST_INT_PORT = 40005; // 不同的port对应不同的socket发送端和接收端

    MulticastSocket broadSocket;// 用于接收广播信息
    DatagramSocket sender;// 数据流套接字 相当于码头，用于发送信息
    InetAddress broadAddress;// 广播地址

    public LanSend() {
        try {

            // 初始化
            broadSocket = new MulticastSocket(BROADCAST_INT_PORT);
            broadAddress = InetAddress.getByName(BROADCAST_IP);

            sender = new DatagramSocket();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("*****LanSend初始化失败*****" + e.toString());
        }
    }

    void join() {
        try {
            broadSocket.joinGroup(broadAddress); // 加入到组播地址，这样就能接收到组播信息
            new Thread(new GetPacket()).start(); // 新建一个线程，用于循环侦听端口信息
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("*****加入组播失败*****");
        }
    }

    // 广播发送查找在线用户
    void sendGetUserMsg() {
        byte[] b = new byte[1024];
        DatagramPacket packet; // 数据包，相当于集装箱，封装信息
        try {
            b = ("find@" + lanDemo.msg).getBytes();
            packet = new DatagramPacket(b, b.length, broadAddress,
                    BROADCAST_INT_PORT); // 广播信息到指定端口
            sender.send(packet);
            System.out.println("*****已发送请求*****");
        } catch (Exception e) {
            System.out.println("*****查找出错*****");
        }
    }

    // 当局域网内的在线机子收到广播信息时响应并向发送广播的ip地址主机发送返还信息，达到交换信息的目的
    void returnUserMsg(String ip) {
        byte[] b = new byte[1024];
        DatagramPacket packet;
        try {
            b = ("retn@" + lanDemo.msg).getBytes();
            packet = new DatagramPacket(b, b.length, InetAddress.getByName(ip),
                    BROADCAST_INT_PORT);
            sender.send(packet);
            System.out.print("发送信息成功！");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("*****发送返还信息失败*****");
        }
    }

    // 当局域网某机子下线是需要广播发送下线通知
    void offLine() {
        byte[] b = new byte[1024];
        DatagramPacket packet;
        try {
            b = ("offl@" + lanDemo.msg).getBytes();
            packet = new DatagramPacket(b, b.length, broadAddress,
                    BROADCAST_INT_PORT);
            sender.send(packet);
            System.out.println("*****已离线*****");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("*****离线异常*****");
        }
    }

    class GetPacket implements Runnable { // 新建的线程，用于侦听
        public void run() {
            DatagramPacket inPacket;

            String[] message;
            while (true) {
                try {
                    inPacket = new DatagramPacket (new byte[1024], 1024);
                    broadSocket.receive(inPacket); // 接收广播信息并将信息封装到inPacket中
                    message = new String(inPacket.getData(), 0,
                            inPacket.getLength()).split("@"); // 获取信息，并切割头部，判断是何种信息（find--上线，retn--回答，offl--下线）

                    if (message[1].equals(lanDemo.ip))
                        continue; // 忽略自身
                    if (message[0].equals("find")) { // 如果是请求信息
                        System.out.println("新上线主机：" + " ip：" + message[1]
                                + " 主机：" + message[2]);
                        returnUserMsg(message[1]);
                    } else if (message[0].equals("retn")) { // 如果是返回信息
                        System.out.println("找到新主机：" + " ip：" + message[1]
                                + " 主机：" + message[2]);
                    } else if (message[0].equals("offl")) { // 如果是离线信息
                        System.out.println("主机下线：" + " ip：" + message[1]
                                + " 主机：" + message[2]);
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("线程出错 " + e);
                }
            }
        }
    }
}

public class lanDemo {
    // 全局变量
    public static String msg;
    public static String ip;
    public static String hostName;

    public static void main(String[] args) { // 程序入口点
        LanSend lSend;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress().toString();
            hostName = addr.getHostName().toString();
            msg = ip + "@" + hostName;
            lSend = new LanSend();
            lSend.join(); // 加入组播，并创建线程侦听

            lSend.sendGetUserMsg(); // 广播信息，寻找上线主机交换信息
            Thread.sleep(10000); // 程序睡眠5秒
            lSend.offLine(); // 广播下线通知
        } catch (Exception e) {
            System.out.println("*****获取本地用户信息出错*****");
        }
    }

}
