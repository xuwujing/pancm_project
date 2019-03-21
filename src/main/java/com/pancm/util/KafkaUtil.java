package com.pancm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The type Kafka util.
 *
 * @author pancm
 * @Title: kafkaUtil
 * @Description: kafka工具类
 * @Version:1.0.0
 * @date 2018年4月2日
 */
public  final class KafkaUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaUtil.class);

    /**
     * 向kafka发送单条消息
     *
     * @param msg       发送的消息
     * @param url       发送的地址
     * @param topicName 消息名称
     * @return boolean
     * @throws Exception the exception
     */
    public static boolean sendMessage(String msg,String url,String topicName) throws Exception{
		KafkaProducer<String, String> producer=null;
		boolean falg=false;
		try{
			Properties props=init(url);
			producer= new KafkaProducer<String, String>(props);
			producer.send(new ProducerRecord<String, String>(topicName,msg));
			falg=true;
		}catch(Exception e){
			logger.error("kafka发送消息失败!",e);
			throw new Exception("向kafka发送消息失败!");
		}finally{
			if(producer!=null){
				producer.close();
			}
		}
		return falg;
	}


    /**
     * 向kafka发送批量消息
     *
     * @param listMsg   发送的消息
     * @param url       发送的地址
     * @param topicName 消息名称
     * @return boolean
     * @throws Exception the exception
     */
    public static boolean sendMessage(List<String> listMsg,String url,String topicName) throws Exception{
		KafkaProducer<String, String> producer=null;
		boolean falg=false;
		try{
			Properties props=init(url);
			producer= new KafkaProducer<String, String>(props);
			for(String msg:listMsg){
				producer.send(new ProducerRecord<String, String>(topicName,msg));
			}
			falg=true;
		}catch(Exception e){
			logger.error("kafka发送消息失败!",e);
			throw new Exception("向kafka发送消息失败!");
		}finally{
			if(producer!=null){
				producer.close();
			}
		}
		return falg;
	}


    /**
     * 向kafka发送批量消息
     *
     * @param listMsg   发送的消息
     * @param url       发送的地址
     * @param topicName 消息名称
     * @param num       每次发送的条数
     * @return boolean
     * @throws Exception the exception
     */
    public static boolean sendMessage(List<String> listMsg,String url,String topicName,int num) throws Exception{
		KafkaProducer<String, String> producer=null;
		boolean falg=false;
		try{
			Properties props=init(url);
			producer= new KafkaProducer<String, String>(props);
			List<String> listMsg2 =new ArrayList<String>();
			for(int i = 1,j = listMsg.size();i<=j;i++){
				listMsg2.add(listMsg.get(i-1));
				if(i%num==0 || i == j){
					producer.send(new ProducerRecord<String, String>(topicName,listMsg2.toString()));
					listMsg2.clear();
				}
			}
			falg=true;
		}catch(Exception e){
			logger.error("kafka发送消息失败!",e);
			throw new Exception("向kafka发送消息失败!");
		}finally{
			if(producer!=null){
				producer.close();
			}
		}
		return falg;
	}
	
	/**
	 * 初始化配置
	 * @param url kafka地址,多个地址则用‘,’隔开
	 * @return
	 */
	private static Properties init(String url){
		Properties props = new Properties();
		props.put("bootstrap.servers", url);
		//acks=0：如果设置为0，生产者不会等待kafka的响应。
		//acks=1：这个配置意味着kafka会把这条消息写到本地日志文件中，但是不会等待集群中其他机器的成功响应。
		//acks=all：这个配置意味着leader会等待所有的follower同步完成。这个确保消息不会丢失，除非kafka集群中所有机器挂掉。这是最强的可用性保证。
		props.put("acks", "all");
		//配置为大于0的值的话，客户端会在消息发送失败时重新发送。
		props.put("retries", 0);
		//当多条消息需要发送到同一个分区时，生产者会尝试合并网络请求。这会提高client和生产者的效率
		props.put("batch.size", 16384);
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());
		return props;
	}
	
}
