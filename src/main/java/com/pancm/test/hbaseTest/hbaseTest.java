package com.pancm.test.hbaseTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.alibaba.fastjson.JSONObject;
/**
 * 
* Title: hbaseTest
* Description: HBase 相关测试
* Version:1.0.0  
* @author pancm
* @date 2017年11月23日
 */
public class hbaseTest {
	
	
    
   /**
    * 批量测试方法
    * @param tableName  表名
    * @param family  列族
    * @param qualifier  列
    * @param value  值
    * @param k		           次数
    */
   public static void insterTest(String tableName,String family,String qualifier,String value, int k){
	   List<JSONObject> list =new ArrayList<>();
		for(int i=1;i<=k;i++){
			JSONObject json =new JSONObject();
			json.put("rowKey", i);              //行健
			json.put("family", family);	 		 //列族
			json.put("qualifier", qualifier);		 //列
			if("t_student".equals(tableName)){ //如果是t_student 姓名则加上编号
				json.put("value", value+i);	//值
			}else if("".equals(value)){		 //如果为空，则是年龄	
				json.put("value", i);	//值
			}else{							  //否则就是性别
				json.put("value", value);	//值
			}
		
			list.add(json);
		}
		HBaseUtil.insertBatch(tableName,list);
   }
    
    
		public static void main(String[] args) {
			test();
//			test1();
		}



	/**
	 * 一些测试
	 */
	private static void test() {
		String tableName1="t_student",tableName2="t_student_info";
		String []columnFamily1={"st1","st2"};
		String []columnFamily2={"stf1","stf2"};
		String rowKey1="1001",rowKey2="";
		String column1="cl1",column2="cl2",column3="clf1",column4="clf2"; 
		String value1="zhangsan",value2="",value3="man";
		
		long starTime=System.currentTimeMillis(); 
		//在t_student 插入100万数据
		insterTest(tableName1,columnFamily1[0],column1,value1,1000001);
		//在t_student_info 插入100万数据
//		insterTest(tableName2,columnFamily2[0],column3,value2,1000001);
//		insterTest(tableName2,columnFamily2[1],column4,value3,1000001);		
		long endTime=System.currentTimeMillis(); 
    	System.out.println("插入数据共使用时间为:"+(endTime-starTime)+"毫秒");
		
//		
//		insert(tableName1,rowKey1,columnFamily1[0],column2,value2);
//		insert(tableName1,rowKey1,columnFamily1[1],column3,value3);
//		insert(tableName1,rowKey1,columnFamily1[1],column4,value4);
//		select(tableName1);
//		select(tableName1,rowKey1,"","");
	}
	
	
	
	/**
	 * 一些测试
	 */
	private static void test1() {
		//定义一个大表
		String tableName="t";
//		String tableName="hive_hbase_test";
//		String tableName="t_student";
		//将关系型数据的表对应成列族 
		String []columnFamily={"t_student","t_class","t_grade","t_subject"};
		HBaseUtil.creatTable(tableName,columnFamily);
        //设置列键(主键，可以一对多)
 		String rowKey1="1",rowKey2="2",rowKey3="3",rowKey4="4";
		//将关系型数据库表字段对应成列
 		String id="id",name="name",age="age"; 
		//数据
 		String id1="1001",name1="张三",age1="18";
 		String id2="1002",name2="李四",age2="20";
		
// 		insert(tableName,"101","t_hive_hbase_test","uid","101");
 		
 		HBaseUtil.insert(tableName,rowKey1,columnFamily[0],id,id1);
 		HBaseUtil.insert(tableName,rowKey1,columnFamily[0],name,name1);
 		HBaseUtil.insert(tableName,rowKey1,columnFamily[0],age,age1);
 		HBaseUtil.insert(tableName,rowKey1,columnFamily[1],id,id1);
 		HBaseUtil.insert(tableName,rowKey1,columnFamily[1],name,name1);
 		HBaseUtil.insert(tableName,rowKey1,columnFamily[1],age,age1);
		
 		HBaseUtil.insert(tableName,rowKey2,columnFamily[0],id,id2);
 		HBaseUtil.insert(tableName,rowKey2,columnFamily[0],name,name2);
 		HBaseUtil.insert(tableName,rowKey2,columnFamily[0],age,age2);
 		HBaseUtil.insert(tableName,rowKey3,columnFamily[3],id,id1);
 		HBaseUtil.insert(tableName,rowKey3,columnFamily[3],name,name1);
 		HBaseUtil.insert(tableName,rowKey3,columnFamily[3],age,age1);
 		long starTime=System.currentTimeMillis(); 
 		HBaseUtil.select(tableName);
 		HBaseUtil.select(tableName,"1001","","");
 		HBaseUtil.select(tableName,rowKey1,columnFamily[0],"");
 		HBaseUtil.select(tableName,rowKey1,columnFamily[0],id);
		long endTime=System.currentTimeMillis(); 
    	System.out.println("查询共使用时间为:"+(endTime-starTime)+"毫秒");
	}
}
