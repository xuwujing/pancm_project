package com.pancm.test.protobufTest.protobuf3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
* @Title: Test
* @Description: 
* @Version:1.0.0  
* @author pancm
* @date 2018年6月12日
*/
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			protobuf3Test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private static void protobuf3Test() throws IOException{
		// 按照定义的数据结构，创建一个Person  
        PersonMsg2.Person.Builder personBuilder = PersonMsg2.Person.newBuilder();  
        personBuilder.setId(1);  
        personBuilder.setName("xuwujing");  
        personBuilder.setEmail("xuwujing@163.com");  
        personBuilder.setFriends("Friend A");  
        personBuilder.setFriends("Friend B");  
        PersonMsg2.Person person = personBuilder.build();  
          
        // 将数据写到输出流，如网络输出流，这里就用ByteArrayOutputStream来代替  
        ByteArrayOutputStream output = new ByteArrayOutputStream();  
        person.writeTo(output);  
          
        // -------------- 分割线：上面是发送方，将数据序列化后发送 ---------------  
          
        byte[] byteArray = output.toByteArray();  
          
        // -------------- 分割线：下面是接收方，将数据接收后反序列化 ---------------  
          
        // 接收到流并读取，如网络输入流，这里用ByteArrayInputStream来代替  
        ByteArrayInputStream input = new ByteArrayInputStream(byteArray);  
          
        // 反序列化  
        PersonMsg2.Person xxg2 = PersonMsg2.Person.parseFrom(input);  
        System.out.println("ID:" + xxg2.getId());  
        System.out.println("name:" + xxg2.getName());  
        System.out.println("email:" + xxg2.getEmail());  
        System.out.println("friend:"+xxg2.getFriends());  
        System.out.println("friend2:"+xxg2.getFriends());  
        
	}
	
}
