package com.pancm.test.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.TimeUnit;

/**
 * The type Zk client api.
 */
public class ZkClientApi {
    
    private final static String connectString = "192.169.0.23:2181";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient(connectString,4000);
        //zkClient一次创建多个节点
        zkClient.createPersistent("/zkclient/zkclient1/zkclient1-1", true);
        System.out.println("创建多层节点成功");
        System.out.println("获取zkclient节点下的子节点："+zkClient.getChildren("/zkclient"));
         
        //zkClient删除一个节点,和原生api一样，只能从最底层节点一个一个删除
        zkClient.delete("/zkclient/zkclient1/zkclient1-1");
        zkClient.createPersistent("/zkclient/zkclient2", "data2");
        zkClient.writeData("/zkclient", "data1");
        zkClient.writeData("/zkclient/zkclient1", "data1-1");
        System.out.println("获取zkclient节点下的子节点："+zkClient.getChildren("/zkclient"));
        System.out.println("获取zkclient节点下的数据："+zkClient.readData("/zkclient"));
        System.out.println("获取zkclient节点下的子节点："+ zkClient.countChildren("/zkclient"));
        System.out.println("获取zkclient节点下的子节点："+zkClient.getChildren("/zkclient/zkclient1"));
       
        //zkClient递归删除某个节点及其子节点
        zkClient.deleteRecursive("/zkclient");
        System.out.println("删除zkclient及其下面的子节点成功\n");
         
        //利用watch机制做订阅,使用异步操作处理节点
        zkClient.subscribeDataChanges("/node", new IZkDataListener(){
            public void handleDataChange(String arg0, Object arg1)
                    throws Exception {
                System.out.println("节点名称："+arg0+"-->修改后的值："+arg1);
            }
 
            public void handleDataDeleted(String arg0) throws Exception {
                System.out.println("删除节点"+arg0+"成功");
            }
        });
        zkClient.createPersistent("/node","node");
        TimeUnit.SECONDS.sleep(2);
        zkClient.writeData("/node", "node1");
        TimeUnit.SECONDS.sleep(2);
        zkClient.delete("/node");
        TimeUnit.SECONDS.sleep(2);
    }
}
