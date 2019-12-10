package com.pancm.test.kafkaTest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;


/**
 * @Author pancm
 * @Description  kafka消费工具类
 * @Date  2019/3/27
 **/
public class KafkaConsumerUtil {
	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerUtil.class);
	private  static Properties props = null;

	
	/**
	 * 获取某一topic的消费者
	 * @param topic
	 * 				订阅的topic
	 * @return
	 * 				对应topic的消费者
	 * @throws Exception 
	 */	
	public static KafkaConsumer<String, String> getConsumer(String topic) throws Exception{
		Objects.requireNonNull(props,"kafka配置为空...");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		//订阅topic
		consumer.subscribe(Arrays.asList(topic));
		return consumer;
	}

	public static KafkaConsumer<String, String> getConsumer2(String topic) throws Exception{
		Objects.requireNonNull(props,"kafka配置为空...");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		return consumer;
	}
	/**
	 * 程序初始化时需要启动
	 * @return
	 * @throws Exception 
	 */
	public synchronized static void init(Map<String,String> config) throws Exception{
		try {
			if(null == props){
				props = new Properties();
				props.put("bootstrap.servers", config.get("bootstrap_servers"));
				props.put("session.timeout.ms", config.get("session_timeout_ms"));
				props.put("max.poll.records", config.get("maxpollrecords"));
				props.put("enable.auto.commit", false);
				props.put("group.id", config.get("groupid").trim());
				props.put("auto.offset.reset", config.get("commitrule"));
				props.put("heartbeat.interval.ms", config.get("heartbeatintervalms"));
//				//序列化
				props.put("key.deserializer", StringDeserializer.class.getName());
				props.put("value.deserializer", StringDeserializer.class.getName());

			}
		} catch (Exception e) {
			throw e;
		} 
	}

	/**
	 * 获取某个topic的分区数量
	 * @param topic
	 * 				topic名
	 * @return
	 * 				分区数
	 * @throws Exception 
	 */
	public static int getTopicPartitions(String topic) throws Exception{
		KafkaConsumer<String, String> consumer = null;
		try {
			consumer = getConsumer(topic);
			List<PartitionInfo> list = consumer.partitionsFor(topic);
			if(null != list && list.size() > 0){
				LOG.debug(topic + "-->topic,分区数量：" + list.size());
				return list.size();
			}else {
				return 1;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if(null != consumer){
				consumer.close();
			}
		}
	}


	/**
	 * 获取某个topic，某个分区的最新offset
	 * @param topic
	 *
	 * @param
	 *
	 * @return
	 */
	public static Map<Integer,Long> getLastOffset(String topic) throws Exception{
		KafkaConsumer<String, String> consumer = null;
		Map<TopicPartition, Long> map =null;
		Map<Integer,Long> map2 = new HashMap<>();
		try {
			consumer = getConsumer(topic);
			List<PartitionInfo> partitionInfoList = consumer.partitionsFor(topic);
			List<TopicPartition> topicPartitions = new ArrayList<>();
			for (PartitionInfo partitionInfo: partitionInfoList) {
				int part =  partitionInfo.partition();
				TopicPartition partitions = new TopicPartition(topic,part);
				topicPartitions.add(partitions);
			}
			//最大的offset
			map= consumer.endOffsets(topicPartitions);
			for (TopicPartition key: map.keySet()) {
				map2.put(key.partition(),map.get(key));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if(consumer!=null){
				consumer.close();
			}
		}
		return  map2;
	}


	public static long getLastOffset2(String topic, int partition) throws Exception{
		long offset = 0L;
		KafkaConsumer<String, String> consumer = null;
		try {
			consumer = getConsumer2(topic);
			List<PartitionInfo> list3 = consumer.partitionsFor(topic);
			List<TopicPartition> list4 = new ArrayList<>();
			for (PartitionInfo partitionInfo: list3) {
				int part =  partitionInfo.partition();
				TopicPartition partitions = new TopicPartition(topic,part);
				list4.add(partitions);
			}
			//最大的offset
			Map<TopicPartition, Long> map= consumer.endOffsets(list4);
			consumer.partitionsFor(topic);
			TopicPartition partitions = new TopicPartition(topic, partition);
			consumer.assign(Arrays.asList(partitions));
			consumer.seekToEnd(Arrays.asList(partitions));
			offset = map.get(topic+"-"+partition);
		} catch (Exception e) {
			throw e;
		} finally {
			if(consumer!=null){
				consumer.close();
			}
		}
		return offset;
	}


	/**
	 * 获取某个topic，某个分区的当前的offset
	 * @param topic
	 *
	 * @param
	 *
	 * @return
	 */
	public static Map<Integer,Long> getCurrentOffset(String topic) throws Exception{
		KafkaConsumer<String, String> consumer = null;
		Map<Integer,Long> map = new HashMap<>();
		try {
			consumer = getConsumer(topic);
			ConsumerRecords<String, String> msgList = consumer.poll(Duration.ofMillis(100));
			if(null!=msgList&&msgList.count()>0) {
				for (ConsumerRecord<String, String> record : msgList) {
					map.put(record.partition(),record.offset());
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if(consumer!=null){
				consumer.close();
			}
		}
		return map;
	}

	/**
	 * 获取某个topic，某个分区的当前的offset
	 * @param topic
	 *
	 * @param partition
	 *
	 * @return
	 */
	public static long getCurrentOffset2(String topic, int partition,String groupid) throws Exception{
		long offset = 0L;
		KafkaConsumer<String, String> consumer = null;
		try {
			consumer = getConsumer(topic);
			TopicPartition partitions = new TopicPartition(topic, partition);
			consumer.assign(Arrays.asList(partitions));
			consumer.subscribe(Arrays.asList(groupid));
			offset = consumer.position(partitions);
		} catch (Exception e) {
			throw e;
		} finally {
			if(consumer!=null){
				consumer.close();
			}
		}
		return offset;
	}
}
