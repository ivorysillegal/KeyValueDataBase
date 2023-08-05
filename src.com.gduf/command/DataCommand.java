package command;

import java.util.HashMap;

public class DataCommand {

    //    传进来想要设置过期时间的键值对
    public static <K, V> long expired(HashMap<K, V> hashMap, K key, long time) {
        scheduleExpiryCheck(hashMap, key, time);
        long expirationTime = System.currentTimeMillis() + time;
        return expirationTime;
    }

//    显示还有多少秒过期
    public static <K, V> void ddl(HashMap<K, V> hashMap, K key, long expirationTime) {
        if (expirationTime > System.currentTimeMillis()) {
            System.out.println(expirationTime - System.currentTimeMillis());
        }
    }


    //    判断键值对是否过期
    private static boolean isExpired(long expirationTime) {
        long now = System.currentTimeMillis();
        return now >= expirationTime;
    }

    //    异步删除过期键值对
    private <K, V> void removeExpired(K key, HashMap<K, V> hashMap) {
        new Thread(() -> hashMap.remove(key)).start();
    }

    //    设置过期时间
    private static <K, V> void scheduleExpiryCheck(HashMap<K, V> hashMap, K key, long time) {
        new Thread(() -> {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                System.out.println("键值对过期机制出错");
                e.printStackTrace();
            }
            hashMap.remove(key);
        });

    }


}
