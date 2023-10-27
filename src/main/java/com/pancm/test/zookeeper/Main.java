package com.pancm.test.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * The type Wa.
 */
class Wa implements Runnable
{

    public void run() {
        //连接启动k
        try {
            ZooKeeper zk = new ZooKeeper("192.169.0.23:2181,192.169.0.24:2181,192.169.0.25:2181", 500000,new Watcher() {
                   // 监控所有被触发的事件
                     public void process(WatchedEvent event) {
                       System.out.println("changing...");
                   }
              });
        //设置监听器 
            Watcher wc = new Watcher() {

                public void process(WatchedEvent event) {
                    // TODO Auto-generated method stub
                    if (event.getType() == EventType.NodeDataChanged) {  
                        System.out.println("change"); 
                    }  
                    if (event.getType() == EventType.NodeDeleted){  
                        System.out.println("dele");  
                    }  
                    if(event.getType()== EventType.NodeCreated){ 
                        System.out.println("create"); 
                }
                }
            };
            //进行轮询，其中exists方法用来询问状态，并且设置了监听器，如果发生变化，则会回调监听器里的方法。
            while(true)
            {
                zk.exists("/zkroot/wangguan", wc);
            }



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeeperException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

/**
 * The type Main.
 */
public class Main {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException          the io exception
     * @throws KeeperException      the keeper exception
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        // TODO Auto-generated method stub

        Thread t = new Thread(new Wa());
        t.start();
    }

}
