package command;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

import static server.DataBaseServer.SET_DATA;

public class SetCommand {

    public SetCommand() {
    }

    public static void sadd(String key, String... values) {
        boolean contains = SET_DATA.containsKey(key);
        if (!contains)
            SET_DATA.put(key, new HashSet<>());

        HashSet<String> set = SET_DATA.get(key);
        set.addAll(Arrays.asList(values));
        SET_DATA.put(key, set);
    }

    public static String smembers(String key) {
        boolean contain = SET_DATA.containsKey(key);
        if (!contain)
            return "null set";

        HashSet<String> set = SET_DATA.get(key);
        Iterator<String> iterator = set.iterator();
        int i = 0;
        StringBuilder msg = new StringBuilder();
        while (iterator.hasNext()) {
            String next = iterator.next();
            msg.append(i++).append(") ").append(next).append("\n");
        }
        return msg.toString();
    }

    public static String sismember(String key, String member) {
        boolean contain = SET_DATA.containsKey(key);
        if (!contain)
            return "null set";

        HashSet<String> set = SET_DATA.get(key);
        boolean contains = set.contains(member);
        if (contains) return "yes";
        else return "no";
    }

    public static String srem(String key, String number) {
        boolean contain = SET_DATA.containsKey(key);
        if (!contain)
            return "null set";

        HashSet<String> set = SET_DATA.get(key);
        if (set.contains(number))
            set.remove(number);
        else
            return "null";
        return "1";
    }

}

