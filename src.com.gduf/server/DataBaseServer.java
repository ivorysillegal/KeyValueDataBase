package server;

import command.IOCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static command.IOCommand.bgsave;
import static message.SecureMessage.encode;
import static server.FileInitialization.*;

public class DataBaseServer {

    public static final int BGSAVE_SECONDS = 30;
    public static final int START_DELAY = 20;
    //        设置保存的间隔时间 和 初始延迟时间
    public static final int corePoolSize = 1;
    public static final int nThreads = 10;
    //    设置执行程序的和定时任务的线程池大小
    public static final Logger logger = LogManager.getLogger(DataBaseServer.class);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        load();
        logger.info("file initialization success");
//        使用FileInitialization类中的静态load方法
//        使用静态方法的时候 加载了他的类 使其中的常量全部得以加载
//        当调用一个类的静态方法时，会首先执行静态初始化块（静态代码块），然后再执行该方法
//        并且静态代码块只会执行一次 完美符合加载常量的要求


        //        设置后台保存的定时任务
        ScheduledExecutorService bgSaver = Executors.newScheduledThreadPool(corePoolSize);
        bgSaver.scheduleAtFixedRate(IOCommand::bgsave, START_DELAY, BGSAVE_SECONDS, TimeUnit.SECONDS);
        logger.debug("bgsave setting success");


        //        注册线程池
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        logger.debug("executorService register success");


        //        程序强制退出或者正常退出时保存数据（利用钩子机制）
        //        注册钩子机制
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!bgSaver.isShutdown()) {
                bgSaver.shutdown();
            }
            if (!executorService.isShutdown())
                executorService.shutdown();
            // 在这里执行你希望在程序退出前保存数据的操作
            IOCommand.save();
            org.tinylog.Logger.info("程序即将退出，保存数据并进行清理操作。");
            logger.info("server disconnected");
        }));
        logger.info("ShutdownHook register success");


//        启动服务器 注册通道 等待连接
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(Integer.parseInt(PORT)));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        org.tinylog.Logger.info("服务器启动成功");
        logger.info("server successfully connected");
//        至此 已经成功启动服务器 下方则是等待客户端的连接

//        空转等待新的连接
        for (; ; ) {

//            获取channel数量 (判断当前有没有空闲的channel)
//            如果没有空闲则空转 继续循环等待连接
            int readyChannels = selector.select();
            if (readyChannels == 0)
                continue;
            logger.debug("get readyChannel");

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                final SelectionKey key = keyIterator.next();
                logger.debug("get Iterator");
                keyIterator.remove();
                if (key.isAcceptable()) {
                    logger.debug("get key to accept");
                    acceptOperator(serverSocketChannel, selector, executorService);
                    logger.debug("key was accepted");
                } else if (key.isReadable()) {
                    logger.debug("get key read");
                    readOperator(selector, key, executorService);
                    logger.debug("key was read");
                }
            }
        }
    }

    private static void readOperator(Selector selector, SelectionKey key, ExecutorService executorService) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
//        处理来自读取的操作

        if (!clientChannel.isConnected()) {
            org.tinylog.Logger.info("连接已断开，关闭通道并取消此注册。");
            logger.warn("client disconnected");
            try {
                clientChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientChannel.keyFor(selector).cancel(); // 取消在选择器上的注册
            logger.warn("finish cancelling client-key");
        } else {
            executorService.submit(() -> {
//            执行具体逻辑
                try {
                    handleReadData(clientChannel, selector, key);
                } catch (IOException e) {
                    org.tinylog.Logger.error("执行读操作错误");
                    logger.error("read error");
                }
            });
        }
    }


    private static void acceptOperator(ServerSocketChannel serverSocketChannel, Selector selector, ExecutorService executorService) throws IOException {
        SocketChannel clientChannel = serverSocketChannel.accept();
//        这个clientChannel是实际连接的客户端
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
//        主线程完成了连接的交接任务后 将真正的执行任务给新的从线程来做

        executorService.submit(() -> {
            try {
                handleClientConnection(clientChannel);
            } catch (IOException e) {
                org.tinylog.Logger.error("服务器已连接上 输出信息错误！");
                logger.warn("client connected and read error");
                e.printStackTrace();
            }
        });
    }

    private static void handleClientConnection(SocketChannel clientChannel) throws IOException {
        // 处理来自客户端的新的连接(已连接)-------连接上了之后执行什么的操作
        String originalMessage = "此客户端已连接！";
        String wrappedMessage;
        try {
            wrappedMessage = encode(originalMessage);
            clientChannel.write(Charset.forName("UTF-8").encode(wrappedMessage));
        } catch (Exception e) {
            logger.error("sending message error");
            org.tinylog.Logger.error("通讯发生错误");
        }
    }

    private static void handleReadData(SocketChannel readyChannel, Selector selector, SelectionKey selectionKey) throws IOException {
//        输出的命令会在这个地方接收
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

//        创建一个空字符串 用来读取内容
        StringBuilder msg = new StringBuilder();

//        读取客户端消息
        int readLength = 0;
        try {
            readLength = readyChannel.read(byteBuffer);

//                处理客户端关闭连接，服务器端在尝试读取时发现连接已被重置，从而引发的异常
        } catch (IOException e) {
            org.tinylog.Logger.info("有连接已断开");
            readLength = -1;
        }
        if (readLength == -1) {
            selectionKey.cancel();
            selectionKey.attach("DISCONNECTED");
            org.tinylog.Logger.info("客户端断开连接 后台保存数据");
            logger.info("client disconnected");
            bgsave();
        }
        if (!"DISCONNECTED".equals(selectionKey.attachment()))
            readyChannel.register(selector, SelectionKey.OP_READ);


        if (readLength > 0) {
//            从默认的写模式切换成读的模式
            byteBuffer.flip();
//            读取内容
            msg.append(Charset.forName("UTF-8").decode(byteBuffer));
            logger.info("reading msg");
            String command = null;
            String[] commands = msg.toString().split(" ");
            int i;
//        遍历数组里面的指令 看一下是否有正确的指令
//            一种数据类型的命令放在一个 String String 的hashmap中
//            例如说 :
//            METHOD[0] 中存放HashMap<String,String>的命令
//            METHODS[1]中存放HashMap<String,HashMap<String,String>>的命令
            for (i = 0; i < METHODS.length; i++) {
                command = METHODS[i].get(commands[0]);
                if (command != null) {
                    break;
                }
//            i 则表示命令对应的数据类型
            }

            boolean execute = false;
//            execute表示方法执行成功与否

            if (command == null) {
                String originalMessage = "'" + commands[0] + "' " + "不是数据库命令";
                String wrappedMessage;
                try {
                    wrappedMessage = encode(originalMessage);
                    readyChannel.write(Charset.forName("UTF-8").encode(wrappedMessage));
                } catch (Exception e) {
                    logger.error("sending message error");
                    org.tinylog.Logger.error("通讯发生错误");
                }

            } else if (command != null) {
//                进入此if分支语句则表示 有存在输入的命令

                org.tinylog.Logger.info("命令输入正确");
                logger.warn("input correct");

                int length = commands.length;
                String[] parameterValues = new String[length - 1];
                System.arraycopy(commands, 1, parameterValues, 0, parameterValues.length);
//                将输入的命令减去第一项 剩下的就是传入方法的形参
//                parameterValues则是后面利用反射执行方法时候 所传入代表可变参数的数组

                try {
                    //            i 则表示命令对应的数据类型
                    org.tinylog.Logger.info("正在执行方法");
                    logger.info("executing methods");
                    execute = Execute(i, readyChannel, parameterValues, command);

                } catch (ClassNotFoundException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    String originalMessage = "调用方法形参出现错误";
                    String wrappedMessage;
                    try {
                        wrappedMessage = encode(originalMessage);
                        readyChannel.write(Charset.forName("UTF-8").encode(wrappedMessage));
                    } catch (Exception e1) {
                        logger.error("sending message error");
                        org.tinylog.Logger.error("通讯发生错误");
                    }
                }
            }
            if (!execute) {
                String originalMessage = "参数输入错误 请重新输入";
                String wrappedMessage;
                try {
                    wrappedMessage = encode(originalMessage);
                    readyChannel.write(Charset.forName("UTF-8").encode(wrappedMessage));
                } catch (Exception e) {
                    logger.error("sending message error");
                    org.tinylog.Logger.error("通讯发生错误");
                }

                logger.info("execute failed");
                org.tinylog.Logger.info("方法执行失败");
                org.tinylog.Logger.info("参数输入错误");
            } else {
                logger.info("execute success");
                org.tinylog.Logger.info("方法执行成功");
            }
        }
    }


    //    通过反射执行方法
    private static boolean Execute(int i, SocketChannel readyChannel, String[] parameterValues, String command) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String className = null;
        switch (i) {
            case 0 -> className = STRING_CLASSNAME;
            case 1 -> className = LINKED_LIST_CLASSNAME;
            case 2 -> className = HASH_CLASSNAME;
            case 3 -> className = SET_CLASSNAME;
            case 4 -> className = IO_CLASSNAME;
            case 5 -> className = DATA_CLASSNAME;
        }
        if (className == null)
            return false;
        Class clazz = Class.forName(className);
        Constructor constructor = clazz.getConstructor();
        Object o = constructor.newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(command)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (method.getParameterCount() != parameterValues.length)
                    return false;
                method.setAccessible(true);

                Object feedback;
                if (parameterTypes.length == 0)
                    feedback = method.invoke(command);
                else
                    feedback = method.invoke(o, (String[]) parameterValues);

//                若有返回值就将返回值传回客户端 没返回值就返回1
                if (feedback == null) {
                        String originalMessage = "1";
                        String wrappedMessage;
                        try {
                            wrappedMessage = encode(originalMessage);
                            readyChannel.write(Charset.forName("UTF-8").encode(wrappedMessage));
                        } catch (Exception e1) {
                            logger.error("sending message error");
                            org.tinylog.Logger.error("通讯发生错误");
                        }
                } else {
                        String wrappedMessage;
                        try {
                            wrappedMessage = encode(String.valueOf(feedback));
                            readyChannel.write(Charset.forName("UTF-8").encode(wrappedMessage));
                        } catch (Exception e) {
                            logger.error("sending message error");
                            org.tinylog.Logger.error("通讯发生错误");
                        }
                }
                return true;
            }
        }
        return false;
    }

}
