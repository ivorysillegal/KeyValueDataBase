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

