package com.pancm.test.jsonTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pancm.util.JsonTools;

import java.util.List;

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
		gson();
	}


	private static void gson(){
		Gson gson = new Gson();

		String m="{\"face_token\":\"377287f36db84114b752cc2e232551f4\",\"user_list\":[{\"score\":91.08332824707,\"group_id\":\"muen_001\",\"user_id\":\"user1\",\"user_info\":\"user's info\"}]}";
		JsonObject jsonObject= gson.fromJson(m, JsonObject.class);
		String k =jsonObject.get("user_list").toString();
		JsonArray jsonArray = gson.fromJson(k,JsonArray.class);
		System.out.println("结果:"+jsonArray.get(0));

    }
}
