package command;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class DataInitialization {

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

    public static <T, U> void saveData(String pathStr,HashMap<T, U> hashMap) {
        try (
                ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(pathStr))
//                在try-with-resources的情况下 用完会自动关流
        ) {
            objectOut.writeObject(hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
