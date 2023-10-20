package com.pancm.test.mapTest;

import java.util.concurrent.*;

/**
 * @Author pancm
 * @Description 创建一个定时map
 * @Date  2023/10/20
 * @Param
 * @return
 **/
public class TimedMap<K, V> extends ConcurrentHashMap<K, V> {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TimedMap() {
        super();
    }

    public TimedMap(int initialCapacity) {
        super(initialCapacity);
    }

    public TimedMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public TimedMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        super(initialCapacity, loadFactor, concurrencyLevel);
    }

    public void putWithTimeout(K key, V value, long timeout, TimeUnit unit) {
        put(key, value);
        scheduler.schedule(() -> remove(key), timeout, unit);
    }

    public static void main(String[] args) throws InterruptedException {
        TimedMap<String, String> timedMap = new TimedMap<>();
        timedMap.putWithTimeout("key1", "value1", 5, TimeUnit.SECONDS);
        for (int i = 0; i < 10; i++) {
            System.out.println(timedMap);
            Thread.sleep(1000);
        }
    }
}
