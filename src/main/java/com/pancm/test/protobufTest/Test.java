package com.pancm.test.protobufTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * The type Test.
 *
 * @author pancm
 * @Title: Test
 * @Description:
 * @Version:1.0.0
 * @date 2018年5月2日
 */
public class Test {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
          
        // 按照定义的数据结构，创建一个Person  
        PersonMsg.Person.Builder personBuilder = PersonMsg.Person.newBuilder();  
        personBuilder.setId(1);  
        personBuilder.setName("xuwujing");  
        personBuilder.setEmail("xuwujing@163.com");  
        personBuilder.addFriends("Friend A");  
        personBuilder.addFriends("Friend B");  
        PersonMsg.Person person = personBuilder.build();  
          
        // 将数据写到输出流，如网络输出流，这里就用ByteArrayOutputStream来代替  
        ByteArrayOutputStream output = new ByteArrayOutputStream();  
        person.writeTo(output);  
          
        // -------------- 分割线：上面是发送方，将数据序列化后发送 ---------------  
          
        byte[] byteArray = output.toByteArray();  
          
        // -------------- 分割线：下面是接收方，将数据接收后反序列化 ---------------  
          
        // 接收到流并读取，如网络输入流，这里用ByteArrayInputStream来代替  
        ByteArrayInputStream input = new ByteArrayInputStream(byteArray);  
          
        // 反序列化  
        PersonMsg.Person xxg2 = PersonMsg.Person.parseFrom(input);  
        System.out.println("ID:" + xxg2.getId());  
        System.out.println("name:" + xxg2.getName());  
        System.out.println("email:" + xxg2.getEmail());  
        System.out.println("friend:");  
        List<String> friends = xxg2.getFriendsList();  
        for(String friend : friends) {  
            System.out.println(friend);  
        }  
    }  
  
}  
