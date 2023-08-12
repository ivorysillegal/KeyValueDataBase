package command;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import static server.DataBaseServer.STRING_DATA;

public class StringCommand {

    public StringCommand() {

    }

    public static void set(String key, String value) {
        STRING_DATA.put(key, value);
    }

    public static String get(String key) {
        return STRING_DATA.get(key);
    }

    public static String del(String key) {
        if (!STRING_DATA.containsKey(key)) {
            return "null";
        } else STRING_DATA.remove(key);
        return "1";
    }
}
