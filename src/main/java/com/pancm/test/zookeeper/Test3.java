package com.pancm.test.zookeeper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 
* @Title: Test
* @Description: 
* zookeeper测试
* @Version:1.0.0  
* @author pancm
* @date 2018年4月28日
 */
public class Test3 {
	private static  ZkClient zk;
	private static  String zkServers = "192.169.0.23:2181,192.169.0.24:2181,192.169.0.25:2181";
	private static  int  CONNECTION_TIMEOUT=30000;
	/**公共目录 */
	private static String COMMON_PATH = "/zkroot/mboss/common";
	/** 私有目录*/
	private static String PRIVATE_PATH = "/zkroot/mboss/1001";
	private  static final String SPRIT ="/";
	
	public static void main(String[] args) throws Exception {
		// 创建一个与服务器的连接
		connection();
		delete("/zkroot/mboss");
		//监听公有目录
		watch(COMMON_PATH);
		//监听私有目录
		watch(PRIVATE_PATH);
		//创建公共目录和私有目录
		create(COMMON_PATH);
		create(PRIVATE_PATH);
		//获取配置
		get(COMMON_PATH,PRIVATE_PATH);
		//创建私有目录配置的名称(key)
		String path1="/zkroot/mboss/1001/name";
		String path2="/zkroot/mboss/common/url";
		create(path1);
		create(path2);
		String data1="admin";
		String data2="admin2";
		String data3="192.169.0.1";
		String data4="192.169.0.2";
		//更新配置参数的值(value),没有就是新增
		setData(path1,data1);
		setData(path2,data3);
		//获取配置
		get(COMMON_PATH,PRIVATE_PATH);
		setData(path1,data2);
		setData(path2,data4);
		//获取配置
		get(COMMON_PATH,PRIVATE_PATH);
		//删除该配置
		delete(path1);
		delete(path2);
		Thread.sleep(3000);
	}
	
	private  static void  watch(String path) {
		  zk.subscribeDataChanges(path, new IZkDataListener(){
	            public void handleDataChange(String dataPath, Object data) throws Exception {
	                System.out.println("监听到该节点:"+dataPath+"下的数据发送了改变!");
	            }

	            public void handleDataDeleted(String dataPath) throws Exception {
	                System.out.println("监听到该节点:"+dataPath+"下的节点被删除了!");
	            }
	        });
		  
		  zk.subscribeChildChanges(path, new IZkChildListener() {
	            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
	                System.out.println("监听到该节点" + parentPath + "的子节点发送了改变!" + currentChilds);
	            }
	        });
		  
		  
		  
	}

	private static void connection() {
		zk=new ZkClient(zkServers,CONNECTION_TIMEOUT);
	}

	
	private static void get(String common_path, String private_path) {
		List<String> commonList =zk.getChildren(common_path);
		List<String> privateList =zk.getChildren(private_path);
		Map<String, String> commonMap = new HashMap<String, String>();
		Map<String, String> privateMap = new HashMap<String, String>();
		for(String yard : commonList){
			String value = zk.readData(common_path.concat(SPRIT).concat(yard));
			String key = yard.substring(yard.indexOf(SPRIT)+1);
			commonMap.put(key, value);
		}
		
		for(String yard : privateList){
			String value = zk.readData(private_path.concat(SPRIT).concat(yard));
			String key = yard.substring(yard.indexOf(SPRIT)+1);
			privateMap.put(key, value);
		}
		privateMap.putAll(commonMap);
		System.out.println("获取最新的数据:"+privateMap);
	}

	private static void delete(String path) {
		if(zk.deleteRecursive(path)){
			System.out.println("成功删除该节点:"+path);
		}
		
	}

	private static void setData(String path, String data) {
		if(zk.exists(path)){
			   zk.writeData(path, data);
			   System.out.println(path+"节点成功写入数据:"+data);
		}
	}

	private static void create(String path) {
		if(!zk.exists(path)){
			zk.createPersistent(path,true);
			System.out.println("成功创建了"+path+"该节点!");
		}
	}
	
}
