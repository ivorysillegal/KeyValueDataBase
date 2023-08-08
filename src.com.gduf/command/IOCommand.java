package command;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

//目前思路：
//创建一个集合 存放配置文件的路径
//在创建一个hashmap的集合 用于和集合一一对应 存放相应类型的hashmap数据进入文件中

public class IOCommand {
    private static boolean saving = false;
    public static ArrayList<String> dataPath;
//    public static void loadStringData(String pathStr) throws IOException {
//        Path path = Paths.get(pathStr);
//        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        asynchronousFileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
//            @Override
//            public void completed(Integer result, ByteBuffer buffer) {
//
//                buffer.flip();
//                byte[] data = new byte[buffer.limit()];
//                buffer.get(data);
//                try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
//                     ObjectInputStream ois = new ObjectInputStream(bis)) {
//                    HashMap<String, String> hashMap = (HashMap<String, String>) ois.readObject();
//                    // 现在你可以在这里使用读取到的HashMap对象
//                    System.out.println(hashMap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void failed(Throwable exc, ByteBuffer attachment) {
//                System.out.println("写入文件错误");
//            }
//        });
//    }

//    public static <T> T loadData(String pathStr,Class<T> dataType) throws IOException {
//        Path path = Paths.get(pathStr);
//        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        asynchronousFileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
//            @Override
//            public void completed(Integer result, ByteBuffer buffer) {
//                buffer.flip();
//                byte[] data = new byte[buffer.limit()];
//                buffer.get(data);
//                try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
//                     ObjectInputStream ois = new ObjectInputStream(bis)) {
//
//                    // Deserialize the data using ObjectInputStream
//                    T loadedData = (T) ois.readObject();
//                    return loadedData;
//                    // Now you can use the loadedData object
//                    System.out.println(loadedData);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void failed(Throwable exc, ByteBuffer attachment) {
//                System.out.println("读取文件错误");
//            }
//        });
//    }


//    public static void loadData(String pathStr, Class dataType) throws IOException {
//        RandomAccessFile dataFile = new RandomAccessFile(pathStr, "rw");
//        FileChannel channel = dataFile.getChannel();
//        ByteBuffer buf = ByteBuffer.allocate(1024);
//        int bytesRead = channel.read(buf);
////        这里返回值是读取的字节的大小
//        while (bytesRead != -1) {
//            System.out.println("读取了" + bytesRead);
//            buf.flip();
////            反转读写模式 从 将数据写入缓冲区 变为 从缓冲区读取数据
//            dataType.getName();
//
//            while (buf.hasRemaining()) {
//                System.out.println((char) buf.get());
//            }
//            buf.clear();
////            clear()切换回写入模式
//            bytesRead = channel.read(buf);
//        }
//
//        dataFile.close();
//        System.out.println("结束");
//
//    }


//    public static <T,U> HashMap<T,U> loadData(String pathStr, Class dataType) throws IOException {
//            HashMap<T, U> hashMap = null;
//            try (FileInputStream fileIn = new FileInputStream(pathStr);
//                 ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
//                hashMap = (HashMap<T,U>) objectIn.readObject();
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            return hashMap;
//    }

    public static <V> HashMap<String, V> load(String path, HashMap<String, V> hashMap) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
        hashMap = (HashMap<String, V>) objectInputStream.readObject();
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        return hashMap;
    }


    //    加载（初始化数据）
    public static <T, U> HashMap<T, U> loadData(String pathStr, Class<HashMap<T, U>> dataType) {
        HashMap<T, U> hashMap = null;
        try (
                ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(pathStr))
        ) {
            hashMap = dataType.cast(objectIn.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    //    保存数据(底层 持久化一中数据类型进入文件中)
    private static void saveFile(String pathStr, HashMap<?, ?> hashMap) {
        try (
                ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(pathStr))
//                在try-with-resources的情况下 用完会自动关流
        ) {
            objectOut.writeObject(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void saveData(ArrayList<HashMap<?, ?>> hashMapArray) {
        for (int i = 0; i < dataPath.size(); i++) {
            saveFile(dataPath.get(i), hashMapArray.get(i));
        }
//        利用for循环依次将所有文件写入
    }


    //            保存指令
    public static void save(ArrayList<HashMap<?, ?>> hashMapArrayList) {
        saveData(hashMapArrayList);
        System.out.println("1");
    }


    //    后台保存指令
    public static void bgsave(ArrayList<HashMap<?, ?>> hashMapArray) {
        if (saving) {
            System.out.println("Another bgsave is already in progress.");
            return;
        }
        saving = true;
//        saving用于标记是否正在进行后台保存操作
//        防止在一个后台保存操作未完成之前 再次触发另一个保存操作
//        避免并发保存操作可能带来的问题

//        创建新的线程来执行后台保存操作
//        Lambda表达式
        new Thread(() -> {
            try {
                saveData(hashMapArray);
            } finally {
                saving = false;
            }
        }).start();
    }


    private static void delFile(String pathsStr) throws IOException {
//        利用输出流默认不续写的特性 将文件覆盖为空文件 达到清空的目的
        FileOutputStream fos = new FileOutputStream(pathsStr);
        fos.close();
        System.out.println("文件内容已清空");
    }


    private static void del() throws IOException {
//        利用for循环依次将所有文件清空
        for (int i = 0; i < dataPath.size(); i++) {
            delFile(dataPath.get(i));
        }
    }

//    删除指令
    public static void flushdb() {
        try {
            del();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void flushdb(HashMap<?, ?> hashMap, ArrayList<HashMap<?, ?>> hashMapArray) {
        for (int i = 0; i < dataPath.size(); i++) {
            if (isSameHashMapType(hashMap, hashMapArray.get(i))){
                try {
                    delFile(dataPath.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("删除特定类型文件出错");
                }
            }
        }
    }

    private static boolean isSameHashMapType(HashMap<?, ?> hashMap1, HashMap<?, ?> hashMap2) {
        Type type1 = hashMap1.getClass().getGenericSuperclass();
        Type type2 = hashMap2.getClass().getGenericSuperclass();
        if (type1 instanceof ParameterizedType && type2 instanceof ParameterizedType) {
            ParameterizedType paramType1 = (ParameterizedType) type1;
            ParameterizedType paramType2 = (ParameterizedType) type2;
            Type[] actualTypeArguments1 = paramType1.getActualTypeArguments();
            Type[] actualTypeArguments2 = paramType2.getActualTypeArguments();
            // 判断键的类型和值的类型是否相同
            if (actualTypeArguments1.length == 2 && actualTypeArguments2.length == 2) {
                return actualTypeArguments1[0].equals(actualTypeArguments2[0]) &&
                        actualTypeArguments1[1].equals(actualTypeArguments2[1]);
            }
        }

        return false;
    }
}
