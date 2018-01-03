package com.pancm.test.storm_kafkaTest;

import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

/**
 * 
* Title: MyKafkaBolt
* Description: 
* Version:1.0.0  
* @author pancm
* @date 2017年12月29日
 */

public class MyKafkaBolt implements IBasicBolt {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
    	//消费的kafka
        String kafkaMsg = input.getString(0) ;
        System.out.println("bolt:"+kafkaMsg);
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        // TODO Auto-generated method stub

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

}
