package command;

import methods.ToLinkedList;

import static server.FileInitialization.KEYS_VALUE;
import static server.FileInitialization.LINKED_LIST_DATA;

public class LinkedListCommand {
    private static ToLinkedList toLinkedList;
    public LinkedListCommand() {
        toLinkedList = new ToLinkedList();
    }


    public String lpush(String key, String input) {
        if (!KEYS_VALUE.contains(key)) {
            toLinkedList.lpush(key, input, LINKED_LIST_DATA);
            return "1";
        } else {
            if ((LINKED_LIST_DATA.get(key)) != null) {
                toLinkedList.lpush(key, input, LINKED_LIST_DATA);
                return "1";
            } else
                return "Duplicate Key";
        }
    }

    public String rpush(String key, String input) {
        if (!KEYS_VALUE.contains(key)) {
           toLinkedList.rpush(key, input, LINKED_LIST_DATA);
            return "1";
        } else {
            if ((LINKED_LIST_DATA.get(key)) != null) {
                toLinkedList.rpush(key, input, LINKED_LIST_DATA);
                return "1";
            } else
                return "Duplicate Key";
        }
    }

    public String range(String key, String start, String end) {
        return toLinkedList.range(key, start, end, LINKED_LIST_DATA);
    }

    public String len(String key) {
        return toLinkedList.len(key, LINKED_LIST_DATA);
    }

    public String lpop(String key) {
        return toLinkedList.lpop(key, LINKED_LIST_DATA);
    }

    public String rpop(String key) {
        return toLinkedList.rpop(key, LINKED_LIST_DATA);
    }

    public String idel(String key) {
        return toLinkedList.idel(key, LINKED_LIST_DATA);
    }

}
