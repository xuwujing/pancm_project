package com.pancm.test.rabbitmqTest.one2one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * The type Client send 1.
 */
public class ClientSend1 {
    /**
     * The constant queue_name.
     */
    public static final String queue_name="my_queue";
    /**
     * The constant durable.
     */
    public static final boolean durable=true; //消息队列持久化

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException              the io exception
     * @throws TimeoutException         the timeout exception
     * @throws KeyManagementException   the key management exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws URISyntaxException       the uri syntax exception
     */
    public static void main(String[] args)
    throws java.io.IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException{
        ConnectionFactory factory=new ConnectionFactory(); //创建连接工厂
//        factory.setHost("localhost");
//        factory.setVirtualHost("my_mq");
//        factory.setUsername("zhxia");
//        factory.setPassword("123456");
        factory.setUri("amqp://guest:guest@172.26.129.3:5672");//获取url
        Connection connection=factory.newConnection(); //创建连接
        Channel channel=connection.createChannel();//创建信道
        channel.queueDeclare(queue_name, durable, false, false, null); //声明消息队列，且为可持久化的
        String message="Hello world"+Math.random();
        //将队列设置为持久化之后，还需要将消息也设为可持久化的，MessageProperties.PERSISTENT_TEXT_PLAIN
        channel.basicPublish("", queue_name, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        System.out.println("Send message:"+message);
        channel.close();
        connection.close();
    }

}