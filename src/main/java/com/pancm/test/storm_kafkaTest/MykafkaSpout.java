package com.pancm.test.storm_kafkaTest;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: MykafkaSpout
 * Description: storm 消费kafka 的主程序
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017年12月29日
 */
public class MykafkaSpout {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    /*
	 * 通过zookeeper进行获取kafka的数据
	 */
    public static void main(String[] args)  {

        String topic = "pcm_test1" ;
        ZkHosts zkHosts = new ZkHosts("192.169.0.23:2181");
        SpoutConfig spoutConfig = new SpoutConfig(zkHosts, topic, 
                "", 
                "MyTrack");
        List<String> zkServers = new ArrayList<String>() ;
        zkServers.add("192.169.0.23");
        spoutConfig.zkServers = zkServers;
        spoutConfig.zkPort = 2181;
        spoutConfig.socketTimeoutMs = 60 * 1000 ;
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme()) ; 

        TopologyBuilder builder = new TopologyBuilder() ;
      //设置1个Executeor(线程)，默认一个
        builder.setSpout("spout", new KafkaSpout(spoutConfig) ,1) ;
      //设置storm 设置1个Executeor(线程) 没有设置Task，默认一个
        builder.setBolt("bolt1", new MyKafkaBolt(), 1).shuffleGrouping("spout") ;

        Config conf = new Config ();
        conf.setDebug(false) ;

        if (args.length > 0) {
        	System.out.println("远程模式");
            try {
				StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			} catch (org.apache.storm.generated.AuthorizationException e) {
				e.printStackTrace();
			}
        }else {
        	System.out.println("本地模式");
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("mytopology", conf, builder.createTopology());
        }

    }

}
