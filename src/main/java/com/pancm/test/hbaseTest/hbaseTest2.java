package com.pancm.test.hbaseTest;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

/**
 * 
* Title: hbaseTest
* Description: hbase的一些测试
* Version:1.0.0  
* @author pancm
* @date 2017年11月8日
 */
public class hbaseTest2 {
	
	
	
	public static void main(String[] args) {
		test1();
		test2();
		
	}
	
	
	 private static void test1(){
		 HBaseUtil.getConnection();
		 System.out.println("查询学生表:");
		 HBaseUtil.select("t_student");
		 System.out.println("查询学生信息表:");
		 HBaseUtil.select("t_student_info");
	 }
	
	 private static void test2(){
	   
	 }
}
