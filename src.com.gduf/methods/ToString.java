package methods;

import java.util.HashMap;

public class ToString {

    public String set(String key, String value, HashMap<String, String> hashMap) {
        hashMap.put(key, value);
        return "1";
    }

    public String get(String key, HashMap<String, String> hashMap) {
        if (!hashMap.containsKey(key)) {
            return "null";
        }
        return hashMap.get(key);
    }

    public String del(String key, HashMap<String, String> hashMap) {
        if (!hashMap.containsKey(key)) {
            return "null";
        } else {
            hashMap.remove(key);
            return "1";
        }
    }
}
