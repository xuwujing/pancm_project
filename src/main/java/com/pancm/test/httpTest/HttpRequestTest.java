package com.pancm.test.httpTest;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ZERO
 * @Data 2017-5-12 下午3:06:29
 * @Description 
 */
public class HttpRequestTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println("123");
		JSONObject json=new JSONObject();
		//Map json=new HashMap();
		String url="http://localhost:8080/Xiaoai101/userLogin.action";
		String url2="http://42.159.192.86:18181/Xiaoai101/userLogin.action";
		json.put("userLogin", "15570895417");
		json.put("userPassword", "123456");
		String data="userLogin=15570895417&userPassword=123456";
		final String url1=url+"?"+data;
		final String url3=url2+"?"+data;
	//	System.out.println(MyHttpClientRequest.httpPost(url2,data));
   //   System.out.println(HttpRequest.doJSONPost(url2, json.toJSONString()));
	//    System.out.println(HttpRequest.doJSONPost(url, data));
	//  System.out.println(HttpUtil.doPost(url, json));
	//    System.out.println(HttpUtil.doPost(url, data));
//      Thread t1 = new Thread(){
//          public void run(){
//              for(int i=0;i<1000;i++){
//            	  System.out.println(i+": "+HttpUtil.doGet1(url1));
//              }
//          }
//      };
//      t1.start();
//	}
		
		for(int i=0;i<1000;i++){
			//Thread.sleep(1000); //一秒发送一次
			 System.out.println(i+": "+HttpUtil.doGet1(url1));
      	 
        }

	}
}
