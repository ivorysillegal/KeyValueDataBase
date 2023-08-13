package server;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static command.IOCommand.loadData;

public class FileInitialization {
    public static HashMap<String, String>[] METHODS;
    //    这个HashMap存储指令和对应方法的映射 目前思路是 服务器激活时通过配置文件读取数据   TBD
//    更新思路 创立一个hashmap的数组 数组的每一项代表一个数据类型的指令及其映射
    public static final String METHODS_PATH = "src.com.gduf\\data\\MethodData.properties";
    public static final String TYPE_PATH = "src.com.gduf\\data\\TypeData.properties";
    public static final String STRING_DATA_PATH = "src.com.gduf\\data\\key_value_data\\StringData.properties";
    public static final String LINKED_LIST_DATA_PATH = "src.com.gduf\\data\\key_value_data\\LinkedListData.properties";
    public static final String HASH_DATA_PATH = "src.com.gduf\\data\\key_value_data\\HashData.properties";
    public static final String SET_DATA_PATH = "src.com.gduf\\data\\key_value_data\\SetData.properties";
    public static final String DATA_PATH_PATH = "src.com.gduf\\data\\PathData.properties";

    public static final String PORT = "8080";
    public static final String STRING_CLASSNAME = "command.StringCommand";
    public static final String LINKED_LIST_CLASSNAME = "command.LinkedListCommand";
    public static final String HASH_CLASSNAME = "command.HashCommand";
    public static final String SET_CLASSNAME = "command.SetCommand";
    public static final String IO_CLASSNAME = "command.IOCommand";
    public static final String DATA_CLASSNAME = "command.DataCommand";

    public static HashMap<String, String> STRING_DATA;
    public static HashMap<String, LinkedList<String>> LINKED_LIST_DATA;
    public static HashMap<String, HashMap<String, String>> HASH_DATA;
    public static HashMap<String, HashSet<String>> SET_DATA;

    public static final int TYPE = 5;
    public static ArrayList<HashMap<?, ?>> TYPE_ARRAY;
    public static LinkedHashMap<String, String> DATA_PATH;


    public static void load(){

        Properties properties = new Properties();

        try {
            FileInputStream fileInputStream = new FileInputStream("config.properties");
            properties.load(fileInputStream);
            fileInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        Field[] fields = FileInitialization.class.getDeclaredFields();
        for (Field field : fields) {
            boolean b = Modifier.isFinal(field.getModifiers());
            if (b){
                try {
                    field.set(null,properties.getProperty(field.getName()));

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }


        DATA_PATH = loadData(DATA_PATH_PATH, DATA_PATH);
        TYPE_ARRAY = loadData(TYPE_PATH);
        METHODS = loadData(METHODS_PATH, TYPE);
        STRING_DATA = loadData(STRING_DATA_PATH, (Class<HashMap<String, String>>) (Class<?>) HashMap.class);
        LINKED_LIST_DATA = loadData(LINKED_LIST_DATA_PATH, (Class<HashMap<String, LinkedList<String>>>) (Class<?>) HashMap.class);
        HASH_DATA = loadData(HASH_DATA_PATH, (Class<HashMap<String, HashMap<String, String>>>) (Class<?>) HashMap.class);
        SET_DATA = loadData(SET_DATA_PATH, (Class<HashMap<String, HashSet<String>>>) (Class<?>) HashMap.class);

    }
}
