package client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class ClientThread implements Runnable {
    private Selector selector;

    public ClientThread(Selector selector) {
        this.selector = selector;

    }

    @Override
    public void run() {
        try {
            for (; ; ) {

                if (!selector.isOpen())
                    break;
//            获取channel数量 (判断当前有没有空闲的channel)
                int readChannels = selector.select();
                if (readChannels == 0) {
                    continue;
                }

//            获取可以使用的channel
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
//            遍历集合
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

//        5 根据就绪状态 调用对应方法实现具体业务操作
//                    这里不需要连接的状态 因为进入了这个线程 相当于已经连接上了
//                    这个线程是专门用来接收服务器的信息的
//        5.2 如果可读状态
                    if (selectionKey.isReadable()) {
                        readOperator(selector, selectionKey);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void readOperator(Selector selector, SelectionKey selectionKey) throws IOException {
        SocketChannel readyChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int readLength = readyChannel.read(byteBuffer);
        String msg = "";
        if (readLength > 0) {
            byteBuffer.flip();
            msg += Charset.forName("UTF-8").decode(byteBuffer);
        }
        readyChannel.register(selector, SelectionKey.OP_READ);
        if (msg.length() > 0)
            System.out.println(msg);
    }
}
