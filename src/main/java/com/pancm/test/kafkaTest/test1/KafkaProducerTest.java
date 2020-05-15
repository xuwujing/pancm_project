package com.pancm.test.kafkaTest.test1;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Title: KafkaProducerTest
 * Description:
 * kafka 生产者demo
 * Version:1.0.0
 *
 * @author pancm
 * @date 2018年1月26日
 */
public class KafkaProducerTest implements Runnable {

	private final KafkaProducer<String, String> producer;
	private final String topic;

//	private String url= "192.169.2.98:2181,192.169.2.188:2181,192.169.2.156:2181";
//	private String url= "192.169.2.30:9092,192.169.2.121:9092,192.169.2.184:9092";
//	private String url= "192.169.0.23:9092,192.169.0.24:9092,192.169.0.25:9092";
//	private String url= "192.169.2.202:9092,192.169.2.248:9092,192.169.2.249:9092";
	private String url= "192.169.2.41:9092";


	/**
     * Instantiates a new Kafka producer test.
     *
     * @param topicName the topic name
     */
    public KafkaProducerTest(String topicName) {
		Properties props = new Properties();
		props.put("bootstrap.servers", url);
		//acks=0：如果设置为0，生产者不会等待kafka的响应。
		//acks=1：这个配置意味着kafka会把这条消息写到本地日志文件中，但是不会等待集群中其他机器的成功响应。
		//acks=all：这个配置意味着leader会等待所有的follower同步完成。这个确保消息不会丢失，除非kafka集群中所有机器挂掉。这是最强的可用性保证。
		props.put("acks", "all");
		//配置为大于0的值的话，客户端会在消息发送失败时重新发送。
		props.put("retries", 1);
		//当多条消息需要发送到同一个分区时，生产者会尝试合并网络请求。这会提高client和生产者的效率
		props.put("batch.size", 16384);
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());
		this.producer = new KafkaProducer<String, String>(props);
		this.topic = topicName;
	}

	@Override
	public void run() {
		int messageNo = 1;
		try {
			for(;;) {
				String messageStr="topic:"+topic+" 你好，这是第"+messageNo+"条数据";
				List<ThreeSpgateBean> list = new ArrayList<>();
				for (int i = 1; i < 10; i++) {
					ThreeSpgateBean threeSpgateBean = new ThreeSpgateBean();
					threeSpgateBean.setEs_status("2");
					threeSpgateBean.setRaw_status("DELIVRD");
					threeSpgateBean.setGateid(String.valueOf(i));
					threeSpgateBean.setMsgtype("1");
					threeSpgateBean.setSpgate("1");
					threeSpgateBean.setSrctype("1");
					threeSpgateBean.setPhone(String.valueOf((12345678901L+i)));
					threeSpgateBean.setSpgate(String.valueOf(1000+i));
					threeSpgateBean.setEs_uptm("2020-03-03 15:59:26.410");
					threeSpgateBean.setRaw_recvtm("2020-03-03 15:59:26.410");
					threeSpgateBean.setRaw_sendtm("2020-03-03 15:59:26.410");
					list.add(threeSpgateBean);
				}


				producer.send(new ProducerRecord<String, String>(topic,  list.toString()));
				//生产了100条就打印
				if(messageNo%100==0){
					System.out.println("发送的信息:" + messageStr);
				}
				//生产1000条就退出
				if(messageNo%10==0){
					System.out.println("成功发送了"+messageNo+"条");
					break;
				}
				messageNo++;
//				Utils.sleep(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
		}
	}

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String args[]) {
		KafkaProducerTest test1 = new KafkaProducerTest("pu_cmd_spgate");
		KafkaProducerTest test2 = new KafkaProducerTest("t2");
		KafkaProducerTest test3= new KafkaProducerTest("t3");
		Thread thread1 = new Thread(test1);
		Thread thread2 = new Thread(test2);
		Thread thread3 = new Thread(test3);

		try {
			thread1.start();
			Thread.sleep(1000);
//			thread2.start();
//			Thread.sleep(1000);
//			thread3.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	

}

class ThreeSpgateBean {

	/** 数据来源 1:号码状态  */
	private String source;

	private String spgate;
	/** 通道ID */
	private String gateid;

	private String es_status;

	private String raw_status;
	/** 手机号 */
	private String phone;
	/** 该条数据在空号ES库中最新的更新时间 */
	private String es_uptm;

	/** 该条数据原始的发送时间(RPT_RVOK.MTSENDTIME) */
	private String raw_sendtm;

	/** 该条数据原始的返回时间(RPT_RVOK.RECVTIME) */
	private String raw_recvtm;


	/** 原运营商类型 取值范围：0,1,2,5,21,255 */
	private String srctype;

	/** 消息类型 */
	private String msgtype;


	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSpgate() {
		return spgate;
	}

	public void setSpgate(String spgate) {
		this.spgate = spgate;
	}

	public String getGateid() {
		return gateid;
	}

	public void setGateid(String gateid) {
		this.gateid = gateid;
	}

	public String getEs_status() {
		return es_status;
	}

	public void setEs_status(String es_status) {
		this.es_status = es_status;
	}

	public String getRaw_status() {
		return raw_status;
	}

	public void setRaw_status(String raw_status) {
		this.raw_status = raw_status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEs_uptm() {
		return es_uptm;
	}

	public void setEs_uptm(String es_uptm) {
		this.es_uptm = es_uptm;
	}

	public String getRaw_sendtm() {
		return raw_sendtm;
	}

	public void setRaw_sendtm(String raw_sendtm) {
		this.raw_sendtm = raw_sendtm;
	}

	public String getRaw_recvtm() {
		return raw_recvtm;
	}

	public void setRaw_recvtm(String raw_recvtm) {
		this.raw_recvtm = raw_recvtm;
	}

	public String getSrctype() {
		return srctype;
	}

	public void setSrctype(String srctype) {
		this.srctype = srctype;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}