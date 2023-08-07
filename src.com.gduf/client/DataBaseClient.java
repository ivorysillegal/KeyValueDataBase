package client;

import command.IOCommand;

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

//        程序强制退出或者正常退出时保存数据（利用钩子机制）
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 在这里执行你希望在程序退出前保存数据的操作
//            IOCommand.save();
            System.out.println("程序即将退出，保存数据并进行清理操作。");
        }));

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

//        创建线程  用于接收服务器返回的信息
        new Thread(new ClientThread(selector)).start();

//        主线程下面的内容用于发信息给客户端
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
