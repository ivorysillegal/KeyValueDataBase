package methods;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

public class ToLinkedList {
    public void lpush(String key, String input, HashMap<String, LinkedList<String>> hashMap) {
        LinkedList<String> value = hashMap.get(key);
        if (value == null) {
            value = new LinkedList<>();
        }
        value.add(0, input);
        hashMap.put(key, value);
    }

    public void rpush(String key, String input, HashMap<String, LinkedList<String>> hashMap) {
        LinkedList<String> value = hashMap.get(key);
        if (value == null) {
            value = new LinkedList<>();
        }
        value.add(input);
        hashMap.put(key, value);
    }

    public String range(String key, String start, String end, HashMap<String, LinkedList<String>> hashMap) {
        LinkedList<String> value = hashMap.get(key);
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

    public String len(String key, HashMap<String, LinkedList<String>> hashMap) {
        LinkedList<String> value = hashMap.get(key);
        if (value == null) {
            return "null LinkedList";
        } else {
            return "len is " + value.size();
        }
    }

    public String lpop(String key, HashMap<String, LinkedList<String>> hashMap) {
        LinkedList<String> value = hashMap.get(key);
        if (value == null) {
            return "null LinkedList";
        }
        String del = value.poll();
        hashMap.put(key, value);
        return "1";
    }

    public String rpop(String key, HashMap<String, LinkedList<String>> hashMap) {
        LinkedList<String> value = hashMap.get(key);
        if (value == null) {
            return "null LinkedList";
        }

        value.pollLast();
        hashMap.put(key, value);
        return "1";
    }

    public String idel(String key, HashMap<String, LinkedList<String>> hashMap) {
        LinkedList<String> value = hashMap.get(key);
        if (value == null) {
            return "null LinkedList";
        }

        value.clear();
        hashMap.put(key, value);
        return "1";
    }
}
