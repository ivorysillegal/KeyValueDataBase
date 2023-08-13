package command;

import java.util.LinkedList;
import java.util.ListIterator;


import static server.FileInitialization.LINKED_LIST_DATA;

public class LinkedListCommand {

    public LinkedListCommand() {
    }

    public static void lpush(String key, String input) {
        LinkedList<String> value = LINKED_LIST_DATA.get(key);
        if (value == null) {
            value = new LinkedList<>();
        }
        value.add(0, input);
        LINKED_LIST_DATA.put(key, value);
    }

    public static void rpush(String key, String input) {
        LinkedList<String> value = LINKED_LIST_DATA.get(key);
        if (value == null) {
            value = new LinkedList<>();
        }

        value.add(input);
        LINKED_LIST_DATA.put(key, value);
    }

    public static String range(String key, String start, String end) {
        LinkedList<String> value = LINKED_LIST_DATA.get(key);
        if (value == null) {
            System.out.println("This LinkedList is null");
            return "null";
        }

        int s = Integer.parseInt(start);
        int e = Integer.parseInt(end);
        ListIterator<String> it = value.listIterator(s);
        StringBuilder msg = new StringBuilder();
        while (it.hasNext() && it.nextIndex() <= e) {
            msg.append(it.next()).append(' ');
        }
        return msg.toString();
    }

    public static String len(String key) {
        LinkedList<String> value = LINKED_LIST_DATA.get(key);
        if (value == null)
            return "null LinkedList";
        else
            return "len is " + value.size();
    }

    public static String lpop(String key) {
        if (LINKED_LIST_DATA.containsKey(key)) {
            LinkedList<String> value = LINKED_LIST_DATA.get(key);
            if (value == null)
                return "null LinkedList";
            String del = value.pop();
            LINKED_LIST_DATA.put(key, value);
            return "1";
        } else {
            return "null LinkedList";
        }
    }


    public static String rpop(String key) {
        if (LINKED_LIST_DATA.containsKey(key)) {
            LinkedList<String> value = LINKED_LIST_DATA.get(key);
            if (value == null)
                return "null LinkedList";

            value.removeLast();
            LINKED_LIST_DATA.put(key, value);
            return "1";
        } else
            return "0";
    }

    public static String idel(String key) {
        LinkedList<String> value = LINKED_LIST_DATA.get(key);
        if (value == null)
            return "null LinkedList";

        value.clear();
        LINKED_LIST_DATA.put(key, value);
        return "1";
    }

}
