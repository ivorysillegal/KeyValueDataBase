package file;

import java.io.*;
import java.util.*;

public class CommandToFile {
    public static void main(String[] args) throws IOException {

        HashMap<String, String> hashMap = new HashMap<>();
        HashMap<String, String> hashMap1 = new HashMap<>();
        HashMap<String, String> hashMap2 = new HashMap<>();
        HashMap<String, String> hashMap3 = new HashMap<>();
        HashMap<String, String> hashMap4 = new HashMap<>();
        HashMap<String, String> hashMap5 = new HashMap<>();

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

        hashMap4.put("save", "save");
        hashMap4.put("flushdb", "flushdb");

        hashMap5.put("expired", "expired");
        hashMap5.put("ddl", "ddl");

        write("src.com.gduf\\data\\command_data\\StringCommand.properties", hashMap);
        write("src.com.gduf\\data\\command_data\\LinkedListCommand.properties", hashMap1);
        write("src.com.gduf\\data\\command_data\\HashCommand.properties", hashMap2);
        write("src.com.gduf\\data\\command_data\\SetCommand.properties", hashMap3);

        HashMap<String, String>[] hashMaps = new HashMap[6];
        hashMaps[0] = hashMap;
        hashMaps[1] = hashMap1;
        hashMaps[2] = hashMap2;
        hashMaps[3] = hashMap3;
        hashMaps[4] = hashMap4;
        hashMaps[5] = hashMap5;
        write("src.com.gduf\\data\\MethodData.properties", hashMaps);
//        对应METHODS 在遍历数组查找命令的时候有用  后期可以更新为ArrayList<>

        HashMap<String, LinkedList<String>> hashMap6 = new HashMap<>();
        HashMap<String, HashMap<String, String>> hashMap7 = new HashMap<>();
        HashMap<String, HashSet<String>> hashMap8 = new HashMap<>();
        HashMap<String, String> hashMap9 = new HashMap<>();
        ArrayList<HashMap<?, ?>> hashMapArrayList = new ArrayList<>();
        hashMapArrayList.add(hashMap6);
        hashMapArrayList.add(hashMap7);
        hashMapArrayList.add(hashMap8);
        hashMapArrayList.add(hashMap9);
        write("src.com.gduf\\data\\TypeData.properties", hashMapArrayList);
//        TypeData说明类型数据 （表示此数据库存储了什么类型的数据 对应TYPE_ARRAY）


//        面向DATA_PATH
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("STRING_DATA_PATH", "src.com.gduf\\data\\key_value_data\\StringData.properties");
        linkedHashMap.put("LINKED_LIST_DATA_PATH", "src.com.gduf\\data\\key_value_data\\LinkedListData.properties");
        linkedHashMap.put("HASH_DATA_PATH", "src.com.gduf\\data\\key_value_data\\HashData.properties");
        linkedHashMap.put("SET_DATA_PATH", "src.com.gduf\\data\\key_value_data\\SetData.properties");
        write("src.com.gduf\\data\\PathData.properties", linkedHashMap);

    }
    public static void write(String filePath, Object hashMap) throws IOException {
        OutputStream fileOutputStream = new FileOutputStream(filePath);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(hashMap);
    }
}
