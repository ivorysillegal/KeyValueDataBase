package command;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import static server.DataBaseServer.LINKED_LIST_DATA;

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
        System.out.println("1");
    }

    public static void rpush(String key, String input) {
        LinkedList<String> value = LINKED_LIST_DATA.get(key);
        if (value == null) {
            value = new LinkedList<>();

        }
        value.add(input);
        LINKED_LIST_DATA.put(key, value);
        System.out.println("1");
    }

    public static void range(String key, String start, String end) {
        LinkedList<String> value = LINKED_LIST_DATA.get(key);
        if (value == null) {
            System.out.println("This LinkedList is null");
            return;
        }
        int s = Integer.parseInt(start);
        int e = Integer.parseInt(end);
        ListIterator<String> it = value.listIterator(s);
        while (it.hasNext() && it.nextIndex() <= e) {
            System.out.print(it.next() + ' ');
        }
        System.out.println();
    }

    public static void len(String key) {
        LinkedList<String> value = LINKED_LIST_DATA.get(key);
        if (value == null) {
            System.out.println("This LinkedList is null");
        } else
            System.out.println("len is " + value.size());
    }

    public static void lpop(String key) {
        if (LINKED_LIST_DATA.containsKey(key)) {
            LinkedList<String> value = LINKED_LIST_DATA.get(key);
            if (value == null) {
                System.out.println("This LinkedList is null");
                return;
            }
            String del = value.pop();
            LINKED_LIST_DATA.put(key, value);
            System.out.println(del);
        } else {
            System.out.println("0");
        }
    }


    public static void rpop(String key) {
        if (LINKED_LIST_DATA.containsKey(key)) {
            LinkedList<String> value = LINKED_LIST_DATA.get(key);
            if (value == null) {
                System.out.println("This LinkedList is null");
                return;
            }
            String del = value.removeLast();
            LINKED_LIST_DATA.put(key, value);
            System.out.println(del);
        } else {
            System.out.println("0");
        }
    }

    public static void idel(String key) {
        LinkedList<String> value = LINKED_LIST_DATA.get(key);
        if (value == null) {
            System.out.println("This LinkedList is already null!");
            return;
        }
        value.clear();
        LINKED_LIST_DATA.put(key, value);
        System.out.println("1");
    }

}
