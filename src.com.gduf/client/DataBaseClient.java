package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class DataBaseClient {

    //    启动客户端的方法
    public void startClient() {
//        连接服务器端
        SocketChannel socketChannel = null;
        Selector selector = null;
        try {

            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
            selector = Selector.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println("服务器连接处出错");
            e.printStackTrace();
        }

//        创建线程
        new Thread(new ClientThread(selector)).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            if (msg.length() > 0) {
                try {
                    socketChannel.write(Charset.forName("UTF-8").encode(msg));
                } catch (IOException e) {
                    System.out.println("输出信息时出错");
                    e.printStackTrace();
                }
            }
        }

    }
}
