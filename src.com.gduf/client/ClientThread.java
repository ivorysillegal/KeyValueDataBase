package client;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import static client.DataBaseClient.mainReturnValue;
import static message.SecureMessage.decode;
import static server.DataBaseServer.logger;

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

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isReadable()) {
                        try {
                            readOperator(selector, selectionKey);
                        } catch (SocketException e) {
                            org.tinylog.Logger.warn("服务器关闭 客户端自动断开连接");
                            logger.warn("server disconnected,client auto-shutdown");
                            System.exit(mainReturnValue);
                        }
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
        if (msg.length() > 0) {
            String originalMessage = null;
            try {
                originalMessage = decode(msg);
            } catch (Exception e) {
                logger.error("sending message error");
                org.tinylog.Logger.error("通讯发生错误");
            }
            System.out.println(originalMessage);
        }
        readyChannel.register(selector, SelectionKey.OP_READ);
    }
}
