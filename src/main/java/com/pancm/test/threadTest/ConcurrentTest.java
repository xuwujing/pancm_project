package com.pancm.test.threadTest;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/5/21
 */
public class ConcurrentTest {
    public static void main(String[] args) {
        //安全的数组
        CopyOnWriteArrayList copyOnWriteArrayList =new CopyOnWriteArrayList();
        copyOnWriteArrayList.add("1");
        copyOnWriteArrayList.add("2");
        copyOnWriteArrayList.add("3");
        System.out.println("======"+copyOnWriteArrayList);

    }
}
