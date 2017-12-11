package com.pancm.test.hive;

import java.sql.SQLException;

/**
 * 
* Title: hiveTest
* Description: hive的JDBC测试 
* 参考 官方文档
* https://cwiki.apache.org/confluence/display/Hive/HiveServer2+Clients#HiveServer2Clients-BeelineExample 
* Version:1.0.0  
* @author pancm
* @date 2017年12月1日
 */
public class hiveTest {
	
  
    
    public static void main(String[] args) throws SQLException {
    	  String sql1 = "select * from t_student where key=1001";
    	  String sql2= "select * from t_student_info where key=1001";
    	  String sql3=" select * from t_student t , t_student_info ti where t.key=ti.key and t.key=1001 ";
    	  long starTime=System.currentTimeMillis(); 
    	  hiveUtil.find(sql1);   	  
    	  hiveUtil.find(sql2);
    	  hiveUtil.find(sql3);
    	  long endTime=System.currentTimeMillis(); 
    	  System.out.println("查询共使用时间为:"+(endTime-starTime)+"毫秒");
	}
    
  
  
 
     
}
