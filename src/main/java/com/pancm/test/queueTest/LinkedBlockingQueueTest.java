package com.pancm.test.queueTest;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2019/7/22
 */
public class LinkedBlockingQueueTest {


    public static void main(String[] args) {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue();
        String msg= "hello";
        //添加一条数据
        System.out.println(queue.add(msg));
        System.out.println(queue.size());
        //拉取一条数据
        System.out.println(queue.poll());
        System.out.println(queue.size());

    }
}
