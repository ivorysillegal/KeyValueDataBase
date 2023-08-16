package command;


import java.util.HashMap;

import static server.FileInitialization.STRING_DATA;

public class StringCommand {

    public StringCommand() {

    }

    public  String set(String key, String value, HashMap<String, String> hashMap) {
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




    public String set(String key, String value) {
        String res = set(key, value, STRING_DATA);
        return res;
    }

    public String get(String key) {
        String res = get(key, STRING_DATA);
        return res;
    }

    public String del(String key) {
        String res = del(key, STRING_DATA);
        return res;
    }

}
