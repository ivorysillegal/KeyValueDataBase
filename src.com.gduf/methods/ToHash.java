package methods;

import java.util.HashMap;

public class ToHash {

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
}
