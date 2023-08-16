package command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static server.FileInitialization.SET_DATA;

public class SetCommand {

    public SetCommand() {
    }

    public void sadd(String key, HashMap<String, HashSet<String>> hashMap, String... values) {
        boolean contains = hashMap.containsKey(key);
        if (!contains) {
            hashMap.put(key, new HashSet<>());
        }

        HashSet<String> set = hashMap.get(key);
        set.addAll(Arrays.asList(values));
        hashMap.put(key, set);
    }

    public String smembers(String key, HashMap<String, HashSet<String>> hashMap) {
        boolean contain = hashMap.containsKey(key);
        if (!contain) {
            return "null set";
        }

        HashSet<String> set = hashMap.get(key);
        Iterator<String> iterator = set.iterator();
        int i = 0;
        StringBuilder msg = new StringBuilder();
        while (iterator.hasNext()) {
            String next = iterator.next();
            msg.append(i++).append(") ").append(next).append("\n");
        }
        return msg.toString();
    }

    public String sismember(String key, String member, HashMap<String, HashSet<String>> hashMap) {
        boolean contain = hashMap.containsKey(key);
        if (!contain) {
            return "null set";
        }

        HashSet<String> set = hashMap.get(key);
        boolean contains = set.contains(member);
        if (contains) {
            return "yes";
        } else {
            return "no";
        }
    }

    public String srem(String key, String number, HashMap<String, HashSet<String>> hashMap) {
        boolean contain = hashMap.containsKey(key);
        if (!contain) {
            return "null set";
        }

        HashSet<String> set = hashMap.get(key);
        if (set.contains(number)) {
            set.remove(number);
        } else {
            return "null";
        }
        return "1";
    }

    public void sadd(String key, String... values) {
        sadd(key, SET_DATA, values);
    }

    public String smembers(String key) {
        return smembers(key, SET_DATA);
    }

    public String sismember(String key, String member) {
        return sismember(key, member, SET_DATA);
    }

    public String srem(String key, String number) {
        return srem(key, number, SET_DATA);
    }


}

