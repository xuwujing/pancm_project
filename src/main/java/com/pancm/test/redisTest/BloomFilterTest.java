package com.pancm.test.redisTest;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:布隆过滤器测试类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/12/4
 */
public class BloomFilterTest {

    private static final int capacity = 1000000;
    private static final int key = 999998;

    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), capacity);

    static {
        for (int i = 0; i < capacity; i++) {
            bloomFilter.put(i);
        }
    }

    public static void main(String[] args) {
        /*返回计算机最精确的时间，单位微妙*/
        long start = System.nanoTime();

        if (bloomFilter.mightContain(key)) {
            System.out.println("成功过滤到" + key);
        }
        long end = System.nanoTime();
        System.out.println("布隆过滤器消耗时间:" + (end - start));
        int sum = 0;
        for (int i = capacity + 20000; i < capacity + 30000; i++) {
            if (bloomFilter.mightContain(i)) {
                sum = sum + 1;
            }
        }
        System.out.println("错判率为:" + sum);
    }

    public String getByKey(String key) {
//        // 通过key获取value
//        String value = redisService.get(key);
//        if (StringUtil.isEmpty(value)) {
//            if (bloomFilter.mightContain(key)) {
//                value = userService.getById(key);
//                redisService.set(key, value);
//                return value;
//            } else {
//                return null;
//            }
//        }
        return null;
    }
}
