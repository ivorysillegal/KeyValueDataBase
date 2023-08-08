package command;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static command.IOCommand.load;

public class DataCommand {

    public DataCommand() {
        try {
            HashMap<String, HashMap<String, String>> hashHashMap = load(HashCommand.HASH_DATA_PATH, HashCommand.HASH_DATA);
            HashMap<String, HashSet<String>> SetHashMap = load(SetCommand.SET_DATA_PATH, SetCommand.SET_DATA);
            HashMap<String, LinkedList<String>> linkedListHashMap = load(LinkedListCommand.LINKED_LIST_DATA_PATH, LinkedListCommand.LINKED_LIST_DATA);
            HashMap<String, String> stringHashMap = load(StringCommand.STRING_DATA_PATH, StringCommand.STRING_DATA);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("加载配置文件时出错");
            e.printStackTrace();
        }
    }


    //    传进来想要设置过期时间的键值对
    public static <V> void expired(String key, String delay) {

    }

    public static <V> void expiredTime(HashMap<String, V> hashMap, String key, long time) {
        scheduleExpiryCheck(hashMap, key, time);
        long expirationTime = System.currentTimeMillis() + time;
    }


    //    显示还有多少秒过期
    public static <V> void ddl(HashMap<String, V> hashMap, String key, long expirationTime) {
        if (expirationTime > System.currentTimeMillis()) {
            System.out.println(expirationTime - System.currentTimeMillis());
        }
    }


    //    判断键值对是否过期
    private static boolean isExpired(long expirationTime) {
        long now = System.currentTimeMillis();
        return now >= expirationTime;
    }

    //    异步删除过期键值对
    private <V> void removeExpired(String key, HashMap<String, V> hashMap) {
        new Thread(() -> hashMap.remove(key)).start();
    }

    //    设置过期时间
    private static <V> void scheduleExpiryCheck(HashMap<String, V> hashMap, String key, long time) {
        new Thread(() -> {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                System.out.println("键值对过期机制出错");
                e.printStackTrace();
            }
            hashMap.remove(key);
        });

    }


}
