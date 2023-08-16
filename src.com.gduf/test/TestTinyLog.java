package test;

import org.tinylog.Logger;

public class TestTinyLog {
    public static void main(String[] args) {
        Logger.error("abcdefg");
        Logger.info("hello world");
        Logger.warn("Yeezus");
        Logger.debug("Utopia");
    }
}
