package command;


import methods.ToString;

import static server.FileInitialization.KEYS_VALUE;
import static server.FileInitialization.STRING_DATA;

public class StringCommand {

    private static ToString toString;

    public StringCommand() {
        toString = new ToString();
    }

    public static String set(String key, String value) {
        if (!KEYS_VALUE.contains(key)) {
            toString.set(key, value, STRING_DATA);
            return "1";
        } else {
            if ((STRING_DATA.get(key)) != null) {
                toString.set(key, value, STRING_DATA);
                return "1";
            } else
                return "Duplicate Key";
        }
    }

    public String get(String key) {
        String res = toString.get(key, STRING_DATA);
        return res;
    }

    public String del(String key) {
        String res = toString.del(key, STRING_DATA);
        return res;
    }

}
