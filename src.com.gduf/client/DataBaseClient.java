package client;

import command.IOCommand;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

import static message.SecureMessage.encode;
import static server.DataBaseServer.logger;
import static server.FileInitialization.HOST_NAME;
import static server.FileInitialization.PORT;
import static message.SecureMessage.decode;

public class DataBaseClient {

    protected static int mainReturnValue;

    //    启动客户端的方法
    public void startClient() {

//        程序强制退出或者正常退出时保存数据（利用钩子机制）
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 在这里执行你希望在程序退出前保存数据的操作
            if (!(mainReturnValue == 111)) {
                try {
                    IOCommand.save();
                    org.tinylog.Logger.info("程序即将退出，保存数据并进行清理操作。");
                    logger.warn("client disconnecting");
                } catch (NullPointerException e) {
                }
            }
        }));

//        连接服务器端
        SocketChannel socketChannel = null;
        Selector selector = null;
        try {

            try {
                socketChannel = SocketChannel.open(new InetSocketAddress(HOST_NAME, Integer.parseInt(PORT)));
            } catch (ConnectException e) {
                org.tinylog.Logger.warn("未启动服务器 请启动服务器后重试");
                logger.warn("server disconnected");
                mainReturnValue = 111;
                System.exit(mainReturnValue);
            }
            selector = Selector.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            org.tinylog.Logger.error("未启动服务器 请启动服务器后重试");
            e.printStackTrace();
        }

//        创建线程  用于接收服务器返回的信息
        new Thread(new ClientThread(selector)).start();

//        主线程下面的内容用于发信息给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            if (msg.equals("exit")) {
                org.tinylog.Logger.warn("客户端正在关闭 即将保存数据");
                try {
                    selector.close();
                    socketChannel.close();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (msg.length() > 0) {
                String wrappedMessage;
                try {
                    wrappedMessage = encode(msg);
                    socketChannel.write(Charset.forName("UTF-8").encode(wrappedMessage));
                } catch (Exception e) {
                    logger.error("sending message error");
                    org.tinylog.Logger.error("通讯发生错误");
                }
            }
        }

    }
}
