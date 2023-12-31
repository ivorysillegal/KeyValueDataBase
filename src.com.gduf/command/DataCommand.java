package command;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static server.DataBaseServer.logger;
import static server.FileInitialization.*;

public class DataCommand {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private static final HashMap<String, ScheduledFuture<?>> expiryTasks = new HashMap<>();
    private static final HashMap<String, Long> taskDeadlines = new HashMap<>();


    public DataCommand() {

    }

    //    设置过期时间
    public static String expired(String key, String delay) {
        int delayTime = Integer.parseInt(delay);
        int type = keyIsExist(key);
        if (type == -1) {
            return "null";
        } else {
            switch (type) {
                case 1:
                    scheduleExpiryCheck(STRING_DATA, key, delayTime);
                case 2:
                    scheduleExpiryCheck(LINKED_LIST_DATA, key, delayTime);
                case 3:
                    scheduleExpiryCheck(HASH_DATA, key, delayTime);
                case 4:
                    scheduleExpiryCheck(SET_DATA, key, delayTime);
            }
        }
        return "1";
    }

    //    判断键是否存在并且判断类型
    private static int keyIsExist(String key) {
        if (STRING_DATA.containsKey(key)) {
            return 1;
        } else if (LINKED_LIST_DATA.containsKey(key)) {
            return 2;
        } else if (HASH_DATA.containsKey(key)) {
            return 3;
        } else if (SET_DATA.containsKey(key)) {
            return 4;
        } else {
            return -1;
        }
    }

    //    传进来想要设置过期时间的键值对
    public static <V> void scheduleExpiryCheck(HashMap<String, V> hashMap, String key, long time) {
        ScheduledFuture<?> expiryTask = scheduler.schedule(() -> {
            hashMap.remove(key);
            expiryTasks.remove(key);
            taskDeadlines.remove(key);
        }, time, TimeUnit.SECONDS);

        expiryTasks.put(key, expiryTask);
        taskDeadlines.put(key, System.currentTimeMillis() + time);
    }

    private static long remainTime(String key) {
        long currentTime = System.currentTimeMillis();
        if (taskDeadlines.containsKey(key)) {
            return taskDeadlines.get(key) - currentTime;
        } else return -1;
    }

    //    查看剩余过期时间
    public static String ddl(String key) {
        long remainTime = remainTime(key);
        if (remainTime == -1) {
            org.tinylog.Logger.info("没有此键");
            logger.info("check error");
            return "null";
        } else return String.valueOf(remainTime);
    }

}
