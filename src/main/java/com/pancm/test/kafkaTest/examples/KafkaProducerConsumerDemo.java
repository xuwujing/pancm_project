package com.pancm.test.kafkaTest.examples;

/**
 * The type Kafka producer consumer demo.
 */
public class KafkaProducerConsumerDemo {

    /**
     * The constant KAFKASTR.
     */
    public static final String KAFKASTR = "master:9092";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		new Producer(KAFKASTR, "pcm_test1").start(); // args[0] 为要发送的 topic
//		new Consumer(KAFKASTR, "pcm_test1").start(); // args[0] 为要接收的 topic
	}
}