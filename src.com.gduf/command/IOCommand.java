package command;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static server.DataBaseServer.logger;
import static server.FileInitialization.*;

//目前思路：
//创建一个集合 存放配置文件的路径
//在创建一个hashmap的集合 用于和集合一一对应 存放相应类型的hashmap数据进入文件中

public class IOCommand {
    private static boolean saving = false;

    //    面向4种数据类型的加载方法
    public static <T, U> HashMap<T, U> loadData(String path, Class<HashMap<T, U>> dataType) {
        HashMap<T, U> hashMap = null;
        Object loadedData;
        ObjectInputStream objectIn;
        try {
            objectIn = new ObjectInputStream(new FileInputStream(path));
            loadedData = objectIn.readObject();
            if (dataType.isInstance(loadedData)) {
                hashMap = dataType.cast(loadedData);
            } else {
                org.tinylog.Logger.warn("类型分配错误");
                logger.error("data cast error");
            }
            objectIn.close();
        } catch (EOFException e) {
            logger.info("blank file");
            hashMap = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        return hashMap;
    }


    //    专门面向METHODS数组
    public static HashMap<String, String>[] loadData(String path, int type) {
        HashMap<String, String>[] hashMaps = null;
        ObjectInputStream objectIn;
        try {
            objectIn = new ObjectInputStream(new FileInputStream(path));
            hashMaps = (HashMap<String, String>[]) objectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (hashMaps == null) {
            hashMaps = new HashMap[type];
        }
        return hashMaps;
    }


    //    专门面向DATA_PATH数组
    public static LinkedHashMap<String, String> loadData(String path, LinkedHashMap<String, String> linkedHashMap) {
        ObjectInputStream objectIn;
        try {
            objectIn = new ObjectInputStream(new FileInputStream(path));
            linkedHashMap = (LinkedHashMap<String, String>) objectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (linkedHashMap == null) {
            linkedHashMap = new LinkedHashMap<>();
        }
        return linkedHashMap;
    }


    //    面向类型数据文件（TYPE_PATH）
    public static ArrayList<HashMap<?, ?>> loadData(String path) {
        ArrayList<HashMap<?, ?>> hashMaps = null;
        ObjectInputStream objectIn;
        try {
            objectIn = new ObjectInputStream(new FileInputStream(path));
            hashMaps = (ArrayList<HashMap<?, ?>>) objectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (hashMaps == null) {
            hashMaps = new ArrayList<>();
        }
        return hashMaps;
    }

    //    面向验证键的唯一性的链表（KEYS_VALUE）
    public static LinkedList<String> loadData(String path,LinkedList<String> linkedList) {
        ObjectInputStream objectIn;
        try {
            objectIn = new ObjectInputStream(new FileInputStream(path));
            linkedList = (LinkedList<String>) objectIn.readObject();
        } catch (EOFException e1){
            linkedList = new LinkedList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (linkedList == null) {
            linkedList = new LinkedList<>();
        }
        return linkedList;
    }


    //    保存数据(底层 持久化一中数据类型进入文件中)
    private static void saveFile(String pathStr, HashMap<?, ?> hashMap) {
        try (
                ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(pathStr, true))
//                在try-with-resources的情况下 用完会自动关流
        ) {
            if (hashMap == null) {
                return;
            }
            objectOut.writeObject(hashMap);
        } catch (EOFException e) {
//            发生此异常的时候表示存储文件为空 创建流的时候就已经出错了 直接不处理
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void saveData(ArrayList<HashMap<?, ?>> hashMapArray) {
        int i = 0;
        for (Map.Entry<String, String> stringEntry : DATA_PATH.entrySet()) {
            saveFile(stringEntry.getValue(), hashMapArray.get(i++));
        }
//        利用for循环依次将所有文件写入
    }


    //            保存指令
    public static void save() {
        ArrayList<HashMap<?, ?>> hashMapArray = new ArrayList<>();
        hashMapArray.add(STRING_DATA);
        hashMapArray.add(LINKED_LIST_DATA);
        hashMapArray.add(SET_DATA);
        hashMapArray.add(HASH_DATA);
        saveData(hashMapArray);
    }


    //    后台保存指令
    public static void bgsave() {
        if (saving) {
            org.tinylog.Logger.warn("另一个后台保存正在执行中");
            logger.warn("bgsave is already working");
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
                org.tinylog.Logger.info("正在保存中");
                logger.info("bgsaving......");
                save();
                logger.info("bgsave success");
                org.tinylog.Logger.info("后台保存成功");
            } finally {
                saving = false;
            }
        }).start();
    }


    private static void delFile(String pathsStr) throws IOException {
//        利用输出流默认不续写的特性 将文件覆盖为空文件 达到清空的目的
        FileOutputStream fos = new FileOutputStream(pathsStr);
        fos.close();
    }


    private static void del() throws IOException {
//        利用for循环依次将所有文件清空
        for (Map.Entry<String, String> stringEntry : DATA_PATH.entrySet()) {
            delFile(stringEntry.getValue());
        }
        logger.info("flushdb success");
        org.tinylog.Logger.info("数据库文件已清空");
    }

    //    删除指令
    public static void flushdb() {
        try {
            del();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    费案 本用于删除单种类型的文件
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
