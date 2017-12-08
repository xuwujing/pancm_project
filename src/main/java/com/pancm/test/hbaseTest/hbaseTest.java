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
	/** hadoop 连接 */
   private static Configuration conf=null;
   /**hbase 连接 */
   private static Connection con=null;
   /** 会话 */
   private static Admin admin = null;
   
   // 初始化连接
   static {
	   // 获得配制文件对象
       conf = HBaseConfiguration.create(); 
       // 设置配置参数
		conf.set("hbase.zookeeper.quorum", "192.169.0.23");
		conf.set("hbase.zookeeper.property.clientPort", "2181");  
		//如果hbase是集群，这个必须加上 
		//ip 和端口是在配置文件配置的
		conf.set("hbase.master", "192.169.0.23:9001"); 
       try {
           con = ConnectionFactory.createConnection(conf);// 获得连接对象
       } catch (IOException e) {
           e.printStackTrace();
           System.exit(1);
       }
   }
	
	
	/**
	 * 创建表
	 * @param tableName 表名
	 * @param columnFamily 列族
	 */
	public static void creatTable(String tableName,String [] columnFamily){
		// 创建表名对象
		TableName tn = TableName.valueOf(tableName);
		// a.判断数据库是否存在
		try {
			//获取会话
			admin = con.getAdmin();
			if (admin.tableExists(tn)) {
				System.out.println(tableName+" 表存在，删除表....");
				// 先使表设置为不可编辑
				admin.disableTable(tn);
				// 删除表
				admin.deleteTable(tn);
				System.out.println("表删除成功.....");
			}
			// 创建表结构对象
			HTableDescriptor htd = new HTableDescriptor(tn);
			for(String str:columnFamily){
				// 创建列族结构对象
				HColumnDescriptor hcd = new HColumnDescriptor(str);
				htd.addFamily(hcd);
			}
			// 创建表
			admin.createTable(htd);
			System.out.println(tableName+" 表创建成功！");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close();
		}
	}
	
	/**
	 * 数据单条插入或更新
	 * @param tableName 	表名
	 * @param rowKey   		行健  (主键)
	 * @param family  		列族
	 * @param qualifier   	列
	 * @param value    		存入的值
	 * @return
	 */
	public static void insert(String tableName, String rowKey,
            String family, String qualifier, String value){
		 Table t=null;
		try {
	             t = con.getTable(TableName.valueOf(tableName));
	            Put put = new Put(Bytes.toBytes(rowKey));
	            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier),
	 	                    Bytes.toBytes(value));
	           t.put(put);
//	          System.out.println(tableName+" 更新成功!");
	        } catch (IOException e) {
	        	System.out.println(tableName+" 更新失败!");
	            e.printStackTrace();
	        } finally {
	            close();
	       }
	}
	
	
	/**
	 * 数据批量插入或更新
	 * @param tableName 	表名
	 * @param list   		hbase的数据
	 * @return
	 */
	public static void insertBatch(String tableName,List<?> list){
		 if(null==tableName||null==list||list.size()==0){
			 return;
		 }
		 Table t=null;	
		 Put put=null;
		 JSONObject json=null;
		 List<Put> puts=new ArrayList<Put>();
		try {
	            t = con.getTable(TableName.valueOf(tableName));
	            for(int i=0,j=list.size();i<j;i++){
	            	json=(JSONObject) list.get(i);
	                put = new Put(Bytes.toBytes(json.getString("rowKey")));
	  	            put.addColumn(Bytes.toBytes(json.getString("family")), Bytes.toBytes(json.getString("qualifier")),
	  	 	                    Bytes.toBytes(json.getString("value")));
	  	           puts.add(put);
	            }
	           t.put(puts);
	          System.out.println(tableName+" 更新成功!");
	        } catch (IOException e) {
	        	System.out.println(tableName+" 更新失败!");
	            e.printStackTrace();
	        } finally {
	            close();
	       }
	}
	
	/**
	 * 查询该表中的所有数据
	 * @param tableName 表名
	 */
	public static void select(String tableName){
		Table t =null;
		 try {
			 t = con.getTable(TableName.valueOf(tableName));
			// 读取操作	
			Scan scan = new Scan();
			// 得到扫描的结果集
			ResultScanner rs = t.getScanner(scan);
			for (Result result : rs) {
				// 得到单元格集合
				List<Cell> cs = result.listCells();
				for (Cell cell : cs) {
					// 取行健
					String rowKey = Bytes.toString(CellUtil.cloneRow(cell));
					// 取到时间戳
					long timestamp = cell.getTimestamp();
					// 取到族列
					String family = Bytes.toString(CellUtil.cloneFamily(cell));
					// 取到修饰名
					String qualifier = Bytes.toString(CellUtil
							.cloneQualifier(cell));
					// 取到值
					String value = Bytes.toString(CellUtil.cloneValue(cell));

					System.out.println("通过表名查询所有:" + " ===> rowKey : " + rowKey
							+ ",  timestamp : " + timestamp + ", family : "
							+ family + ", qualifier : " + qualifier
							+ ", value : " + value);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据条件明细查询
	 * @param tableName 	表名
	 * @param rowKey   		行健  (主键)
	 * @param family  		列族
	 * @param qualifier   	列
	 */
	public static void select(String tableName,String rowKey,String family, String qualifier){
		System.out.println();
		Table t =null;
		 try {
			 t = con.getTable(TableName.valueOf(tableName));
			// 通过HBase中的 get来进行查询
			Get get = new Get(Bytes.toBytes(rowKey));
			if(null!=family&&family.length()>0){
				if(null!=qualifier&&qualifier.length()>0){
					get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
				}else{
					get.addFamily(Bytes.toBytes(family));
				}
			}
			Result r = t.get(get);
			List<Cell> cs = r.listCells();
			if(null==cs||cs.size()==0){
				return;
			}
				for (Cell cell : cs) {
					String rowKey1 = Bytes.toString(CellUtil.cloneRow(cell)); // 取行键
					long timestamp = cell.getTimestamp(); // 取到时间戳
					String family1 = Bytes.toString(CellUtil.cloneFamily(cell)); // 取到族列
					String qualifier1 = Bytes.toString(CellUtil.cloneQualifier(cell)); // 取到列
					String value = Bytes.toString(CellUtil.cloneValue(cell)); // 取到值
					System.out.println("通过条件查询:"+" ===> rowKey : " + rowKey1
							+ ",  timestamp : " + timestamp + ", family : "
							+ family1 + ", qualifier : " + qualifier1 + ", value : "
							+ value);
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 连接关闭
	 */
    public static void close() {
    	try {
    		if(admin!=null){
			   admin.close();
    		}
//    		 if (con != null) {
//    			 con.close();
//    		 }
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
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
		insertBatch(tableName,list);
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
 		creatTable(tableName,columnFamily);
        //设置列键(主键，可以一对多)
 		String rowKey1="1",rowKey2="2",rowKey3="3",rowKey4="4";
		//将关系型数据库表字段对应成列
 		String id="id",name="name",age="age"; 
		//数据
 		String id1="1001",name1="张三",age1="18";
 		String id2="1002",name2="李四",age2="20";
		
// 		insert(tableName,"101","t_hive_hbase_test","uid","101");
 		
		insert(tableName,rowKey1,columnFamily[0],id,id1);
		insert(tableName,rowKey1,columnFamily[0],name,name1);
		insert(tableName,rowKey1,columnFamily[0],age,age1);
		insert(tableName,rowKey1,columnFamily[1],id,id1);
		insert(tableName,rowKey1,columnFamily[1],name,name1);
		insert(tableName,rowKey1,columnFamily[1],age,age1);
		
		insert(tableName,rowKey2,columnFamily[0],id,id2);
		insert(tableName,rowKey2,columnFamily[0],name,name2);
		insert(tableName,rowKey2,columnFamily[0],age,age2);
		insert(tableName,rowKey3,columnFamily[3],id,id1);
		insert(tableName,rowKey3,columnFamily[3],name,name1);
		insert(tableName,rowKey3,columnFamily[3],age,age1);
 		long starTime=System.currentTimeMillis(); 
		select(tableName);
		select(tableName,"1001","","");
		select(tableName,rowKey1,columnFamily[0],"");
		select(tableName,rowKey1,columnFamily[0],id);
		long endTime=System.currentTimeMillis(); 
    	System.out.println("查询共使用时间为:"+(endTime-starTime)+"毫秒");
	}
}
