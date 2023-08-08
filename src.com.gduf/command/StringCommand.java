package command;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class StringCommand {

    public static HashMap<String, String> STRING_DATA;
    private static final String STRING_DATA_PATH = "src.com.gduf\\data\\key_value_data\\StringData.properties";

    public StringCommand() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(STRING_DATA_PATH));
            STRING_DATA = (HashMap<String, String>) objectInputStream.readObject();
            if (STRING_DATA == null) {
                STRING_DATA = new HashMap<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("加载集合类型配置文件时出错");
            e.printStackTrace();
        }
    }

    public static void set(String key, String value) {
        STRING_DATA.put(key, value);
    }

    public static void get(String key) {
        STRING_DATA.get(key);
    }

    public static void del(String key) {
        STRING_DATA.remove(key);
    }
}
