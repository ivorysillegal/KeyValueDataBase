package test;

import command.HashCommand;
import command.LinkedListCommand;
import command.SetCommand;
import command.StringCommand;
import methods.ToHash;
import methods.ToLinkedList;
import methods.ToSet;
import methods.ToString;
import to_api.KeyValueDataBase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


public class TestApi {
    public static void main(String[] args) {
        String key = "1";
        String value = "2";

        HashMap<String, String> hashMap = new HashMap<>();
        ToString string = KeyValueDataBase.String();
        string.set(key, value, hashMap);

        KeyValueDataBase.String().set(key, value, hashMap);
        KeyValueDataBase.String().get(key, hashMap);
        KeyValueDataBase.String().del(key, hashMap);


        HashMap<String, LinkedList<String>> linkedListHashMap = new HashMap<>();
        ToLinkedList linkedList = KeyValueDataBase.LinkedList();
        linkedList.lpush(key, value, linkedListHashMap);

        KeyValueDataBase.LinkedList().rpush(key, value, linkedListHashMap);
        KeyValueDataBase.LinkedList().lpush(key, value, linkedListHashMap);
        KeyValueDataBase.LinkedList().len(key, linkedListHashMap);


        HashMap<String, HashMap<String, String>> hashMapHashMap = new HashMap<>();
        ToHash hashCommand = KeyValueDataBase.Hash();
        String field = "3";
        hashCommand.hset(key, field, value, hashMapHashMap);

        KeyValueDataBase.Hash().hset(key, field, value, hashMapHashMap);
        KeyValueDataBase.Hash().hget(key, field, hashMapHashMap);
        KeyValueDataBase.Hash().hdel(key, field, hashMapHashMap);


        HashMap<String, HashSet<String>> setHashMap = new HashMap<>();
        ToSet setCommand = KeyValueDataBase.Set();
        setCommand.sadd(key, setHashMap, value);

        KeyValueDataBase.Set().sadd(key, setHashMap, value);
        KeyValueDataBase.Set().sismember(key, value, setHashMap);
        KeyValueDataBase.Set().smembers(key, setHashMap);


    }
}
