package command;

import methods.ToHash;

import static server.FileInitialization.*;

public class HashCommand {

    private static ToHash toHash;

    public HashCommand() {
        toHash = new ToHash();
    }

    public String hset(String key, String field, String value) {
        if (!KEYS_VALUE.contains(key)) {
            toHash.hset(key, field, value, HASH_DATA);
            return "1";
        } else {
            if ((HASH_DATA.get(key)) != null) {
                toHash.hset(key, field, value, HASH_DATA);
                return "1";
            } else
                return "Duplicate Key";
        }
    }

    public String hget(String key, String field) {
        return toHash.hget(key, field, HASH_DATA);
    }

    public String hdel(String key, String field) {
        return toHash.hdel(key, field, HASH_DATA);
    }

    public String hdel(String key) {
        return toHash.hdel(key, HASH_DATA);
    }

}
