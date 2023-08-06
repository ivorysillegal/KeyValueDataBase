package file;

import java.io.*;
import java.util.HashMap;

public class LogCommandToFile {
    public static void main(String[] args) throws IOException {

        HashMap<String, String> hashMap = new HashMap<>();
        HashMap<String, String> hashMap1 = new HashMap<>();
        HashMap<String, String> hashMap2 = new HashMap<>();
        HashMap<String, String> hashMap3 = new HashMap<>();

        hashMap.put("set", "set");
        hashMap.put("get", "get");
        hashMap.put("del", "del");

        hashMap1.put("lpush", "lpush");
        hashMap1.put("rpush", "rpush");
        hashMap1.put("range", "range");
        hashMap1.put("len", "len");
        hashMap1.put("lpop", "lpop");
        hashMap1.put("rpop", "rpop");
        hashMap1.put("idel", "idel");

        hashMap2.put("hset", "hset");
        hashMap2.put("hget", "hget");
        hashMap2.put("hdel", "hdel");

        hashMap3.put("sadd", "sadd");
        hashMap3.put("smembers", "smembers");
        hashMap3.put("sismember", "sismember");
        hashMap3.put("srem", "srem");

//        write("src.com.gduf\\data\\StringData.properties", hashMap);
        write("src.com.gduf\\data\\LinkedListData.properties", hashMap1);
        write("src.com.gduf\\data\\HashData.properties", hashMap2);
        write("src.com.gduf\\data\\SetData.properties", hashMap3);
    }

    public static void write(String filePath, HashMap<String, String> hashMap) throws IOException {
        OutputStream fileOutputStream = new FileOutputStream(filePath);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(hashMap);
    }


}
