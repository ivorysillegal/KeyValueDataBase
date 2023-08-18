package command;

import methods.ToSet;

import static server.FileInitialization.KEYS_VALUE;
import static server.FileInitialization.SET_DATA;

public class SetCommand {
    private static ToSet toSet;
    public SetCommand() {
        toSet = new ToSet();
    }

    public String sadd(String key, String... values) {
        if (!KEYS_VALUE.contains(key)) {
            toSet.sadd(key, SET_DATA, values);
            return "1";
        } else {
            if ((SET_DATA.get(key)) != null) {
                toSet.sadd(key, SET_DATA, values);
                return "1";
            } else
                return "Duplicate Key";
        }
    }

    public String smembers(String key) {
        return toSet.smembers(key, SET_DATA);
    }

    public String sismember(String key, String member) {
        return toSet.sismember(key, member, SET_DATA);
    }

    public String srem(String key, String number) {
        return toSet.srem(key, number, SET_DATA);
    }


}

