package command;


import static server.FileInitialization.STRING_DATA;

public class StringCommand {

    public StringCommand() {

    }

    public static String set(String key, String value) {
        if (!STRING_DATA.containsKey(key))
            return "null";
        STRING_DATA.put(key, value);
        return "1";
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
