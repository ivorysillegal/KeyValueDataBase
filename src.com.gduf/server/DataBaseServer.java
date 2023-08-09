package server;

import command.IOCommand;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static command.IOCommand.*;

public class DataBaseServer {

    private static HashMap<String, String>[] METHODS;
    //    这个HashMap存储指令和对应方法的映射 目前思路是 服务器激活时通过配置文件读取数据   TBD
//    更新思路 创立一个hashmap的数组 数组的每一项代表一个数据类型的指令及其映射
    private static final String METHODS_PATH = "src.com.gduf\\data\\MethodData.properties";
    private static final int PORT = 8080;
    private static final String STRING_CLASSNAME = "command.StringCommand";
    private static final String LINKED_LIST_CLASSNAME = "command.LinkedListCommand";
    private static final String HASH_CLASSNAME = "command.HashCommand";
    private static final String SET_CLASSNAME = "command.SetCommand";
    public static HashMap<String, HashMap<String, String>> HASH_DATA;
    public static final String HASH_DATA_PATH = "src.com.gduf\\data\\key_value_data\\HashData.properties";
    public static HashMap<String, LinkedList<String>> LINKED_LIST_DATA;
    public static final String LINKED_LIST_DATA_PATH = "src.com.gduf\\data\\key_value_data\\LinkedListData.properties";
    public static HashMap<String, HashSet<String>> SET_DATA;
    public static final String SET_DATA_PATH = "src.com.gduf\\data\\key_value_data\\SetData.properties";
    public static HashMap<String, String> STRING_DATA;
    public static final String STRING_DATA_PATH = "src.com.gduf\\data\\key_value_data\\StringData.properties";
    public static final int Type = 5;
    public static ArrayList<HashMap<?, ?>> TYPE_ARRAY;
    public static final String TYPE_PATH = "src.com.gduf\\data\\TypeData.properties";
    public static HashMap<String, String> DATA_PATH;
    public static final String IO_CLASSNAME = "command.IOCommand";
    public static final String DATA_CLASSNAME = "command.DataCommand";

    public static void main(String[] args) throws IOException {

        TYPE_ARRAY = loadData(TYPE_PATH);
        METHODS = loadData(METHODS_PATH, Type);
        STRING_DATA = loadData(STRING_DATA_PATH, (Class<HashMap<String, String>>) (Class<?>) HashMap.class);
        LINKED_LIST_DATA = loadData(LINKED_LIST_DATA_PATH, (Class<HashMap<String, LinkedList<String>>>) (Class<?>) HashMap.class);
        HASH_DATA = loadData(HASH_DATA_PATH, (Class<HashMap<String, HashMap<String, String>>>) (Class<?>) HashMap.class);
        SET_DATA = loadData(SET_DATA_PATH, (Class<HashMap<String, HashSet<String>>>) (Class<?>) HashMap.class);

        DATA_PATH.put("METHODS_PATH", "src.com.gduf\\data\\MethodData.properties");
        DATA_PATH.put("HASH_DATA_PATH", "src.com.gduf\\data\\key_value_data\\HashData.properties");
        DATA_PATH.put("LINKED_LIST_DATA_PATH", "src.com.gduf\\data\\key_value_data\\LinkedListData.properties");
        DATA_PATH.put("SET_DATA_PATH", "src.com.gduf\\data\\key_value_data\\SetData.properties");
        DATA_PATH.put("STRING_DATA_PATH", "src.com.gduf\\data\\key_value_data\\StringData.properties");
        DATA_PATH.put("TYPE_PATH", "src.com.gduf\\data\\TypeData.properties");


//        启动服务器 注册通道 等待连接
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功");
//        至此 已经成功启动服务器 下方则是等待客户端的连接

//        注册线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

//        空转等待新的连接
        for (; ; ) {

//            获取channel数量 (判断当前有没有空闲的channel)
//            如果没有空闲则空转 继续循环等待连接
            int readyChannels = selector.select();
            if (readyChannels == 0)
                continue;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                final SelectionKey key = keyIterator.next();
                keyIterator.remove();
//                使用了此个selectionKey之后 迭代器中的空闲通道减少 所以在此处要再次获取迭代器

//                判断就绪的selectionKey的状态
                if (key.isAcceptable()) {
//                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
//                    SocketChannel clientChannel = serverChannel.accept();
//                   上面的注释掉的第一行代码是多余的 因为在accept方法中已经获取到Selection中的key了
//                   但是除了accept方法其他都需要获取key
                    acceptOperator(serverSocketChannel, selector, executorService);

                } else if (key.isReadable()) {

                    readOperator(selector, key, executorService);

                } else if (key.isWritable()) {

                    writeOperator(selector, key, executorService);

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
//        处理来自读取的操作
        executorService.submit(new Runnable() {
            @Override
            public void run() {
//            执行具体逻辑
                try {
                    handleReadData(clientChannel, selector);
                } catch (IOException e) {
                    System.out.println("执行读的操作的时候出错");
                    e.printStackTrace();
                }
            }
//            只要通道仍然注册在选择器上，并且不需要更改其关注的事件的时候
//            不需要在每次执行操作后重新注册通道
        });
    }


    private static void acceptOperator(ServerSocketChannel serverSocketChannel, Selector selector, ExecutorService executorService) throws IOException {
        SocketChannel clientChannel = serverSocketChannel.accept();
//        这个clientChannel是实际连接的客户端
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
//        主线程完成了连接的交接任务后 将真正的执行任务给新的从线程来做

        // Hand off the new connection to a worker thread for handling
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // Handle new client connection
                try {
                    handleClientConnection(clientChannel);
                } catch (IOException e) {
                    System.out.println("服务器已连接上 输出信息错误！");
                    e.printStackTrace();
                }
            }
        });
    }

    private static void handleClientConnection(SocketChannel clientChannel) throws IOException {
        // 处理来自客户端的新的连接(已连接)-------连接上了的话你想执行什么 操作 可以选择在这里记录报文或者日志
        clientChannel.write(Charset.forName("UTF-8").encode("此客户端已连接！"));
    }


    private static void handleReadData(SocketChannel readyChannel, Selector selector) throws IOException {
//        输出的命令会在这个地方接收
//        这里应该利用反射获取方法
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//        循环读取客户端消息
        int readLength = readyChannel.read(byteBuffer);
//        创建一个空字符串 用来读取内容
        String msg = "";
        if (readLength > 0) {
//            从默认的写模式切换成读的模式
            byteBuffer.flip();
//            读取内容
            msg += Charset.forName("UTF-8").decode(byteBuffer);
        }

        String command = null;
        String[] commands = msg.split(" ");
        int i;
//        遍历数组里面的指令 看一下是否有正确的指令
//            一种数据类型的命令放在一个 String String 的hashmap中
//            例如说 :
//            METHOD[0] 中存放HashMap<String,String>的命令
//            METHODS[1]中存放HashMap<String,HashMap<String,String>>的命令
        for (i = 0; i < METHODS.length; i++) {
            command = METHODS[i].get(commands[0]);
//            i 则表示命令对应的数据类型
//            重名的命令处理  TBD
//            看是否能获取到相同名字的命令
        }

        boolean execute = false;
//            flag表示方法执行成功与否

        if (command != null) {
//                进入此if分支语句则表示 有存在输入的命令

            int length = commands.length;
            String[] parameterValues = new String[length - 1];
            System.arraycopy(commands, 1, parameterValues, 0, parameterValues.length);
//                将输入的命令减去第一项 剩下的就是传入方法的形参
//                parameterValues则是后面利用反射执行方法时候 所传入代表可变参数的数组

            try {
                switch (i) {
                    //            i 则表示命令对应的数据类型
                    case 0 -> execute = Execute(parameterValues, command, STRING_CLASSNAME);
                    case 1 -> execute = Execute(parameterValues, command, LINKED_LIST_CLASSNAME);
                    case 2 -> execute = Execute(parameterValues, command, HASH_CLASSNAME);
                    case 3 -> execute = Execute(parameterValues, command, SET_CLASSNAME);
                    case 4 -> execute = Execute(parameterValues, command, IO_CLASSNAME);
                    case 5 -> execute = Execute(parameterValues, command, DATA_CLASSNAME);
                }
            } catch (ClassNotFoundException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                System.out.println("调用方法形参出现错误");
                readyChannel.write(Charset.forName("UTF-8").encode("调用方法形参出现错误"));
            }
            if (!execute) {
                readyChannel.write(Charset.forName("UTF-8").encode("参数输入错误 请重新输入"));
                readyChannel.write(Charset.forName("UTF-8").encode("'" + commands[0] + "' " + "不是数据库命令"));
            }
        }

        readyChannel.register(selector, SelectionKey.OP_READ);
    }


    //    通过反射执行方法
    private static boolean Execute(String[] parameterValues, String command, String className) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Class clazz = Class.forName(className);
        Constructor constructor = clazz.getConstructor();
        Object o = constructor.newInstance();
        Method method = clazz.getDeclaredMethod(command);
        if (method.getParameterCount() != parameterValues.length) {
            return false;
        }
        method.setAccessible(true);
        method.invoke(o, (Object) parameterValues);
        return true;
    }

}
