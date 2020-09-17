package com.pancm.test.cache;

import com.pancm.test.pojoTest.User;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: cache缓存测试类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/17
 */
public class CacheTest {
    public static void main(String[] args) {
        /**
         * Cacheable ： 主要针对方法配置，能够根据方法的请求参数对其结果进行缓存
         *
         * value : 必须要的。
         * key ： 可选。要使用SpEL表达式
         * condition：可选，是否需要缓存
         * unless:
         *

         *
         *
         **/
        CacheTest cacheTest = new CacheTest();
        System.out.println(cacheTest.getUser("xuwujing"));
        System.out.println(cacheTest.getUser("xuwujing"));
        System.out.println(cacheTest.getUser("pancm"));
        System.out.println(cacheTest.getUser("xuwujing",0));
        System.out.println(cacheTest.getUser("xuwujing",1));
        System.out.println(cacheTest.getUser("xuwujing",1));


    }


    @Cacheable(value = "user", key="#userName")
    public  User getUser(String userName){
        User user = new User();
        user.setId(1);
        user.setName(userName);
        return user;
    }

    @Cacheable(value = "user", key="#user",condition = "#status = 1",unless="#result == null")
    public  User getUser(String userName,Integer status){
        if(status == 0){
            return null;
        }
        User user = new User();
        user.setId(2);
        user.setName(userName);
        return user;
    }



}
