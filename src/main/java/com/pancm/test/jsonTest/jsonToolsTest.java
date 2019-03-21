package com.pancm.test.jsonTest;

import com.alibaba.fastjson.JSONObject;
import com.pancm.util.JsonTools;

/**
 * Title: jsonToolsTest
 * Description: fastjson工具包测试
 * Version:1.0.0
 *
 * @author panchengming
 * @date 2017年9月10日
 */
public class jsonToolsTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
	   JSONObject json=new JSONObject();
	   json.put("id", 1);
	   json.put("name", "张三");
	   json.put("age", 22);
	   User user= JsonTools.toBean(json, User.class);
	   User user1= JsonTools.toBean(json.toJSONString(), User.class);
	   User user2=new User();
	   user2.setAge(20);
	   user2.setId(5);
	   user2.setName("王五");
	   JSONObject json2=JsonTools.toJson(user2);
	   System.out.println("user:"+user);
	   System.out.println("user1:"+user1);
	   System.out.println("json2:"+json2);
	   
	   JSONObject json3=new JSONObject();
	   json3.put("id", 1);
	   json3.put("name", "张三");
	   json3.put("age", 22);
 	   System.out.println("json3:"+json3);
 	   System.out.println("json3:"+json3.getString("qq"));
	}
}
