package com.pancm.test.hbaseTest;

import java.io.IOException;
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

/**
 * 
* Title: HBaseUtil
* Description: HBase工具类
* Version:1.0.0  
* @author pancm
* @date 2017年12月6日
 */
public class HBaseUtil {
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
		 * 获取连接
		 * @return
		 */
		public synchronized static Connection getConnection(){
			if (null == con||con.isClosed()) {
				try {
					 con = ConnectionFactory.createConnection(conf);// 获得连接对象
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("获取数据库连接失败！");
				}
			}
			
			return con;
			
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
		 * 数据插入或更新
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
//	    		 if (con != null) {
//	    			 con.close();
//	    		 }
	    	} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
}
