package command;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class SetCommand {
    public static HashMap<String, HashSet<String>> SET_DATA;
    public static final String SET_DATA_PATH = "src.com.gduf\\data\\key_value_data\\SetData.properties";

    public SetCommand() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(SET_DATA_PATH));
            SET_DATA = (HashMap<String, HashSet<String>>) objectInputStream.readObject();
            if (SET_DATA == null) {
                SET_DATA = new HashMap<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("加载集合类型配置文件时出错");
            e.printStackTrace();
        }
    }

    public static void sadd(String key, String... values) {
        HashSet<String> set = SET_DATA.get(key);
        set.addAll(Arrays.asList(values));
        SET_DATA.put(key, set);
    }

    public static void smembers(String key) {
        HashSet<String> set = SET_DATA.get(key);
        Iterator<String> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(i + ") " + next);
        }
    }

    public static void sismember(String key, String member) {
        HashSet<String> set = SET_DATA.get(key);
        boolean contains = set.contains(member);
        if (contains) System.out.println("yes");
        else System.out.println("no");
    }

    public static void srem(String key, String number) {
        HashSet<String> set = SET_DATA.get(key);
        if (set.contains(number))
            set.remove(number);
        else
            System.out.println("null");
    }
}

