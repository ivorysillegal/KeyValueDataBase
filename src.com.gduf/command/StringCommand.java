package command;

import java.util.HashMap;

public class StringCommand {

    public static HashMap<String,String> STRING_DATA;

    public static void set(String key,String value){
        STRING_DATA.put(key,value);
    }

    public static void get(String key){
        STRING_DATA.get(key);
    }

    public static void del(String key){
        STRING_DATA.remove(key);
    }
}
