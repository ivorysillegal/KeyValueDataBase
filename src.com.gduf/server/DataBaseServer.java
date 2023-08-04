package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataBaseServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功");
//        至此 已经成功启动服务器 下方则是等待客户端的连接

//        注册线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (; ; ) {

//            获取channel数量 (判断当前有没有空闲的channel)
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                continue;
            }

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                final SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isAcceptable()) {

//                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
//                    SocketChannel clientChannel = serverChannel.accept();
//                   上面的注释掉的第一行代码是多余的 因为在accept方法中已经获取到Selection中的key了
//                   但是除了accept方法其他都需要获取key

                    acceptOperator(serverSocketChannel, selector, executorService);
                } else if (key.isReadable()) {

                    readOperator(selector, key, executorService);

                }else if (key.isWritable()){

                    writeOperator(selector,key, executorService);

                }
            }
        }
    }

    private static void writeOperator(Selector selector, SelectionKey key, ExecutorService executorService) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        // Hand off the read data to a worker thread for handling
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // Handle read data from the client
                handleWriteData(clientChannel);
            }
        });

    }

    private static void handleWriteData(SocketChannel clientChannel) {

    }

    private static void readOperator(Selector selector, SelectionKey key, ExecutorService executorService) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        // Hand off the read data to a worker thread for handling
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // Handle read data from the client
                try {
                    handleReadData(clientChannel,selector);
                } catch (IOException e) {
                    System.out.println("执行读的操作的时候出错");
                    e.printStackTrace();
                }
            }
        });
    }

    private static void handleClientConnection(SocketChannel clientChannel) {
        // 处理来自客户端的新的连接(已连接)-------连接上了的话你想执行什么 操作 可以选择在这里记录报文或者日志

    }

    private static void acceptOperator(ServerSocketChannel serverSocketChannel, Selector selector, ExecutorService executorService) throws IOException {
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);

        // Hand off the new connection to a worker thread for handling
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // Handle new client connection
                handleClientConnection(clientChannel);
            }
        });
    }

    private static void handleReadData(SocketChannel readyChannel, Selector selector) throws IOException {
        // Handle read data from the client
        // ...
        //        2 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//        3 循环读取客户端消息
        int readLength = readyChannel.read(byteBuffer);
//        创建一个空字符串 用来读取内容
        String msg = "";
        if (readLength > 0) {
//            切换成读的模式
            byteBuffer.flip();
//            读取内容
            msg += Charset.forName("UTF-8").decode(byteBuffer);


        }
//        4 将channel再次注册到选择器上 监听可读状态
        readyChannel.register(selector, SelectionKey.OP_READ);
//        5 把客户端发送信息 广播到其他客户端
        if (msg.length() > 0)
            System.out.println(msg);
    }
}
