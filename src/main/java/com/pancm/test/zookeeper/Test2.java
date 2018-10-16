package com.pancm.test.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * 
* @Title: Test2
* @Description: 
* zookeeper 的其他测试
* @Version:1.0.0  
* @author pancm
* @date 2018年5月2日
 */
public class Test2 {
	private static String url="192.169.0.23:2181";
	private static  ZooKeeper zk;
	private static  int  CONNECTION_TIMEOUT=30000;
	
	
	
	public static void main(String[] args) {
		watch();
		
		
	}
	
	/**
	 * 启动监听
	 */
	private static void watch(){
		// 创建一个与服务器的连接
		 try {
			zk = new ZooKeeper(url , 
					 CONNECTION_TIMEOUT, new Watcher() { 
			            // 监控所有被触发的事件
			            public void process(WatchedEvent event) { 
			                System.out.println("已经触发了" + event.getType() + "事件！"); 
			            } 
			        });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
