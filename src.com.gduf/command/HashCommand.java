package command;

import java.util.HashMap;

public class HashCommand {
    public static HashMap<String,HashMap<String,String>> HASH_DATA;

    public static void hset(String key, String field, String value) {
        HashMap<String, String> data = HASH_DATA.get(key);
//            如果是第一次写入 或者之前清空过 data是null不能直接写入
        if (data == null) data = new HashMap<>();
        data.put(field, value);
//                把修改完后的数值重新写入文件 以便下一次写入文件
        HASH_DATA.put(key, data);
        System.out.println("1");
    }

    public static void hget(String key, String field) {
        if (HASH_DATA.containsKey(key)) {
            HashMap<String, String> data = HASH_DATA.get(key);
            if (data == null) {
                System.out.println("null");
                return;
            }
            if (data.containsKey(field)) {
                String s = data.get(field);
                System.out.println(s);
            } else {
                System.out.println("null");
            }
        } else {
            System.out.println("null");
        }
    }

    public static void hdel(String key, String field) {
        if (HASH_DATA.containsKey(key)) {
            HashMap<String, String> data = HASH_DATA.get(key);
            if (data == null) {
                System.out.println("null");
                return;
            }
            if (data.containsKey(field)) {
                data.remove(field);
                HASH_DATA.put(key, data);
                System.out.println("1");
//                把修改完后的数值重新写入文件 以便下一次写入文件
            } else {
                System.out.println("null");
            }
        } else {
            System.out.println("null");
        }
    }

    public static void hdel(String key) {
        if (HASH_DATA.containsKey(key)) {
            HashMap<String, String> data = HASH_DATA.get(key);
            if (data == null) {
                System.out.println("null");
                return;
            }
            data.clear();
            System.out.println("1");
        } else {
            System.out.println("null");
        }
    }

}
