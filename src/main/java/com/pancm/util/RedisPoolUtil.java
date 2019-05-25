package com.pancm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/3/30
 */

public class RedisPoolUtil {

    private static final Logger log = LoggerFactory.getLogger(RedisPoolUtil.class);

    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("expire key:{} error",key,e);
            RedisPool.close(jedis);
            return result;
        }
        RedisPool.close(jedis);
        return result;
    }

    //exTime的单位是秒
    public static String setEx(String key,String value,int exTime){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value,e);
            RedisPool.close(jedis);
            return result;
        }
        RedisPool.close(jedis);
        return result;
    }

    public static String set(String key,String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            RedisPool.close(jedis);
            return result;
        }
        RedisPool.close(jedis);
        return result;
    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            RedisPool.close(jedis);
            return result;
        }
        RedisPool.close(jedis);
        return result;
    }

    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
            RedisPool.close(jedis);
            return result;
        }
        RedisPool.close(jedis);
        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();

        jedis.setex("name",100,"lzh");

        System.out.println("end");


    }


}
