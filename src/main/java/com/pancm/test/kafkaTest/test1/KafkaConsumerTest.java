package com.pancm.test.kafkaTest.test1;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * @Title: KafkaConsumerTest
 * @Description:
 * kafka消费者 消费多个topic
 * 并且可以暂停其中一个topic消费
 * @Version:1.0.0
 * @author pancm
 * @date 2018年1月26日
 */
public class KafkaConsumerTest implements Runnable {

	private final KafkaConsumer<String, String> consumer;
	private ConsumerRecords<String, String> msgList;
	private  String[] topic;
	private  String topic1;
	private static final String GROUPID = "group1";

    //主程序的日志打印
    private static Logger LOG = LoggerFactory.getLogger(KafkaConsumerTest.class);

    private long pause=1;
//    private String url= "192.169.2.30:9092,192.169.2.121:9092,192.169.2.184:9092";
//	private String url= "192.169.0.23:9092,192.169.0.24:9092,192.169.0.25:9092";
	private String url= "192.169.2.202:9092,192.169.2.248:9092,192.169.2.249:9092";
    /**
     * Instantiates a new Kafka consumer test.
     *
     * @param topicName the topic name
     */
    public KafkaConsumerTest(String[] topicName) {
		Properties props = new Properties();
		//kafka消费的的地址
		props.put("bootstrap.servers", url);
		//组名 不同组名可以重复消费
		props.put("group.id", GROUPID);
		//是否自动提交
		props.put("enable.auto.commit", "false");
		//从poll(拉)的回话处理时长
		props.put("auto.commit.interval.ms", "1000");
		//超时时间
		props.put("session.timeout.ms", "30000");
		//一次最大拉取的条数
		props.put("max.poll.records", 100);
//		earliest当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费 
//		latest 
//		当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据 
//		none 
//		topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
		props.put("auto.offset.reset", "earliest");
		//序列化
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		this.consumer = new KafkaConsumer<String, String>(props);
		this.topic = topicName;
		//订阅主题列表topic
		this.consumer.subscribe(Arrays.asList(topic));
		
	}

	@Override
	public void run() {
		int messageNo = 1;
         topic1 = topic[2];
        List<PartitionInfo> list = consumer.partitionsFor(topic1);
        List<TopicPartition> list2 = new ArrayList<>();
        for (PartitionInfo partitionInfo: list) {
            int part =  partitionInfo.partition();
            TopicPartition partitions = new TopicPartition(topic1,part);
            list2.add(partitions);
        }

		//最大的offset
		Map<TopicPartition, Long> map= consumer.endOffsets(list2);
		LOG.info("最大的offset:{}",map);
        System.out.println("---------开始消费---------");
		try {
			for (;;) {
					msgList = consumer.poll(100);
					if(null!=msgList&&msgList.count()>0){
					for (ConsumerRecord<String, String> record : msgList) {
                        LOG.info("topic:"+record.topic()+"=======receive: partition = " + record.partition() + ", value = " + record.value()+" offset==="+record.offset());
                        //暂停一个topic
                        if(pause == 1000){
//							if(topic1.equals(record.topic())){
//								LOG.warn("topic:"+record.topic()+"=======receive:partition = " + record.partition()+"=======receive:offset = " + record.offset());
//							}
							consumer.pause(list2);
							LOG.info("topic:"+topic1+ "暂停数据拉取！");
                        }else if(pause == 2000){
//							if(topic1.equals(record.topic())){
//								LOG.warn("topic:"+record.topic()+"=======receive:partition = " + record.partition()+"=======receive:offset = " + record.offset());
//							}
//							LOG.warn("结束运行!");
//							System.exit(0);
                            consumer.resume(list2);
                            LOG.info("topic:"+topic1+ "恢复数据拉取！");
                        }
                        pause++;
						//当消费了100条就退出
//						if(messageNo%6000==0){
//							System.exit(0);
//						}
						messageNo++;
						consumer.commitSync();

					}
				}else{	
					Thread.sleep(5000);
					System.out.println("====");
				}
			}		
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String args[]) {
//    	String []topics={"MT_RVOK","RPT_RVOK","MT_SDOK"};
    	String []topics={"t4","t5","t6"};
		KafkaConsumerTest test1 = new KafkaConsumerTest(topics);
		Thread thread1 = new Thread(test1);
		thread1.start();
	}
}
