package server;

import java.io.FileInputStream;

import java.util.*;

import static command.IOCommand.loadData;

public class FileInitialization {
    public static HashMap<String, String>[] METHODS;
    //    这个HashMap存储指令和对应方法的映射 目前思路是 服务器激活时通过配置文件读取数据   TBD
//    更新思路 创立一个hashmap的数组 数组的每一项代表一个数据类型的指令及其映射
    public static final String METHODS_PATH;
    public static final String TYPE_PATH;
    public static final String STRING_DATA_PATH;
    public static final String LINKED_LIST_DATA_PATH;
    public static final String HASH_DATA_PATH;
    public static final String SET_DATA_PATH;
    public static final String DATA_PATH_PATH;

    public static final String HOST_NAME;
    public static final String PORT;
    public static final String STRING_CLASSNAME;
    public static final String LINKED_LIST_CLASSNAME;
    public static final String HASH_CLASSNAME;
    public static final String SET_CLASSNAME;
    public static final String IO_CLASSNAME;
    public static final String DATA_CLASSNAME;

    public static HashMap<String, String> STRING_DATA;
    public static HashMap<String, LinkedList<String>> LINKED_LIST_DATA;
    public static HashMap<String, HashMap<String, String>> HASH_DATA;
    public static HashMap<String, HashSet<String>> SET_DATA;

    public static final int TYPE = 5;
    public static ArrayList<HashMap<?, ?>> TYPE_ARRAY;
    public static LinkedHashMap<String, String> DATA_PATH;

    static {

        Properties properties = new Properties();

        try {
            FileInputStream fileInputStream = new FileInputStream("src.com.gduf\\data\\Config.properties");
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        METHODS_PATH = properties.getProperty("METHODS_PATH");
        TYPE_PATH = properties.getProperty("TYPE_PATH");
        STRING_DATA_PATH = properties.getProperty("STRING_DATA_PATH");
        LINKED_LIST_DATA_PATH = properties.getProperty("LINKED_LIST_DATA_PATH");
        HASH_DATA_PATH = properties.getProperty("HASH_DATA_PATH");
        SET_DATA_PATH = properties.getProperty("SET_DATA_PATH");
        DATA_PATH_PATH = properties.getProperty("DATA_PATH_PATH");
        PORT = properties.getProperty("PORT");
        HOST_NAME = properties.getProperty("HOST_NAME");

        STRING_CLASSNAME = properties.getProperty("STRING_CLASSNAME");
        LINKED_LIST_CLASSNAME = properties.getProperty("LINKED_LIST_CLASSNAME");
        HASH_CLASSNAME = properties.getProperty("HASH_CLASSNAME");
        SET_CLASSNAME = properties.getProperty("SET_CLASSNAME");
        IO_CLASSNAME = properties.getProperty("IO_CLASSNAME");
        DATA_CLASSNAME = properties.getProperty("DATA_CLASSNAME");
    }

    public static void load() {
        DATA_PATH = loadData(DATA_PATH_PATH, DATA_PATH);
        TYPE_ARRAY = loadData(TYPE_PATH);
        METHODS = loadData(METHODS_PATH, TYPE);
        STRING_DATA = loadData(STRING_DATA_PATH, (Class<HashMap<String, String>>) (Class<?>) HashMap.class);
        LINKED_LIST_DATA = loadData(LINKED_LIST_DATA_PATH, (Class<HashMap<String, LinkedList<String>>>) (Class<?>) HashMap.class);
        HASH_DATA = loadData(HASH_DATA_PATH, (Class<HashMap<String, HashMap<String, String>>>) (Class<?>) HashMap.class);
        SET_DATA = loadData(SET_DATA_PATH, (Class<HashMap<String, HashSet<String>>>) (Class<?>) HashMap.class);
    }
}
