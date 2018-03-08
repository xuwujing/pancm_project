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
		String url="http://blog.csdn.net/qazwsxpcm/article/details/79405554";
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
			Thread.sleep(1000); //一秒发送一次
			 System.out.println(i+": "+HttpUtil.doGet1(url));
      	 
        }

	}
}
