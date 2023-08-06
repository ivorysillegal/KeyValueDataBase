package command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class SetCommand {
    public static HashMap<String, HashSet<String>> SET_DATA;

    public static void sadd(String key, String... values) throws ClassNotFoundException {
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

