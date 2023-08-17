package command;

import java.util.HashMap;

import static server.FileInitialization.*;

public class HashCommand {
    public HashCommand() {

    }

    public void hset(String key, String field, String value, HashMap<String, HashMap<String, String>> hashMap) {
        HashMap<String, String> data = hashMap.get(key);
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(field, value);
        hashMap.put(key, data);
    }

    public String hget(String key, String field, HashMap<String, HashMap<String, String>> hashMap) {
        if (hashMap.containsKey(key)) {
            HashMap<String, String> data = hashMap.get(key);
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

    public String hdel(String key, String field, HashMap<String, HashMap<String, String>> hashMap) {
        if (hashMap.containsKey(key)) {
            HashMap<String, String> data = hashMap.get(key);
            if (data == null) {
                return "null HashMap";
            }

            if (data.containsKey(field)) {
                data.remove(field);
                hashMap.put(key, data);
                return "1";
            } else {
                return "null HashMap";
            }
        } else {
            return "null HashMap";
        }
    }

    public String hdel(String key, HashMap<String, HashMap<String, String>> hashMap) {
        if (hashMap.containsKey(key)) {
            HashMap<String, String> data = hashMap.get(key);
            if (data == null) {
                return "null HashMap";
            }
            data.clear();
            return "1";
        } else {
            return "null HashMap";
        }
    }

    public String hset(String key, String field, String value) {
        if (!KEYS_VALUE.contains(key)) {
            hset(key, field, value, HASH_DATA);
            return "1";
        } else {
            if ((HASH_DATA.get(key)) != null) {
                hset(key, field, value, HASH_DATA);
                return "1";
            } else
                return "Duplicate Key";
        }
    }

    public String hget(String key, String field) {
        return hget(key, field, HASH_DATA);
    }

    public String hdel(String key, String field) {
        return hdel(key, field, HASH_DATA);
    }

    public String hdel(String key) {
        return hdel(key, HASH_DATA);
    }

}
