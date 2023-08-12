package command;

import java.util.HashMap;

import static server.DataBaseServer.HASH_DATA;

public class HashCommand {
    public HashCommand() {

    }

    public static void hset(String key, String field, String value) {
        HashMap<String, String> data = HASH_DATA.get(key);
//            如果是第一次写入 或者之前清空过 data是null不能直接写入
        if (data == null) data = new HashMap<>();
        data.put(field, value);
//                把修改完后的数值重新写入文件 以便下一次写入文件
        HASH_DATA.put(key, data);
    }

    public static String hget(String key, String field) {
        if (HASH_DATA.containsKey(key)) {
            HashMap<String, String> data = HASH_DATA.get(key);
            if (data == null) {
                return "null HashMap";
            }
            if (data.containsKey(field)) {
                String s = data.get(field);
                System.out.println(s);
            } else {
                return "null HashMap";
            }
        } else {
            return "null HashMap";
        }
        return "null HashMap";
    }

    public static String hdel(String key, String field) {
        if (HASH_DATA.containsKey(key)) {
            HashMap<String, String> data = HASH_DATA.get(key);
            if (data == null)
                return "null HashMap";

            if (data.containsKey(field)) {
                data.remove(field);
                HASH_DATA.put(key, data);
                return "1";
//                把修改完后的数值重新写入文件 以便下一次写入文件
            } else {
                return "null HashMap";
            }
        } else {
            return "null HashMap";
        }
    }

    public static String hdel(String key) {
        if (HASH_DATA.containsKey(key)) {
            HashMap<String, String> data = HASH_DATA.get(key);
            if (data == null) {
                return "null HashMap";
            }
            data.clear();
            return "1";
        } else {
            return "null HashMap";
        }
    }

}
