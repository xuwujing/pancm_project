package com.pancm.test.hbaseTest;

import java.io.IOException;
import java.util.Arrays;
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
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
/**
 * 
* Title: hbaseTest
* Description: hbase的一些测试
* Version:1.0.0  
* @author pancm
* @date 2017年11月23日
 */
public class MyHbaseApi {


	public static void main(String[] args) {
		Admin admin = null;
		Connection con = null;

		try {
			// 1.获得配置文件对象
			Configuration conf = HBaseConfiguration.create();
			/* 设置配置参数 
			 * hserver3 从本地的hosts中读取 路径(C:\WINDOWS\system32\drivers\etc)
			 */
			conf.set("hbase.zookeeper.quorum", "hserver3");
			conf.set("hbase.zookeeper.property.clientPort", "2181");
			// 2.建立连接
			con = ConnectionFactory.createConnection(conf);
			// 3.获得会话
			admin = con.getAdmin();
			// System.out.println(con);
			// System.out.println(admin);
			// 4.操作
			// 建立数据库
			// 创建表名对象
			TableName tn = TableName.valueOf("abc");
			// a.判断数据库是否存在
			if (admin.tableExists(tn)) {
				System.out.println("====> 表存在，删除表....");
				// 先使表设置为不可编辑
				admin.disableTable(tn);
				// 删除表
				admin.deleteTable(tn);
				System.out.println("表删除成功.....");
			}
			System.out.println("===>表不存在,创建表......");
			// 创建表结构对象
			HTableDescriptor htd = new HTableDescriptor(tn);
			// 创建列族结构对象
			HColumnDescriptor hcd1 = new HColumnDescriptor("fm1");
			HColumnDescriptor hcd2 = new HColumnDescriptor("fm2");
			htd.addFamily(hcd1);
			htd.addFamily(hcd2);
			// 创建表
			admin.createTable(htd);

			System.out.println("创建表成功...");

			// 向表中插入数据
			// a.单个插入
			Put put = new Put(Bytes.toBytes("row01"));// 参数是行健row01
			put.addColumn(Bytes.toBytes("fm1"), Bytes.toBytes("col1"),
					Bytes.toBytes("value01"));

			// 获得表对象
			Table table = con.getTable(tn);
			table.put(put);

			// 批量插入
			Put put01 = new Put(Bytes.toBytes("row02"));// 参数是行健row02
			put01.addColumn(Bytes.toBytes("fm2"), Bytes.toBytes("col2"),
					Bytes.toBytes("value02")).addColumn(Bytes.toBytes("fm2"),
					Bytes.toBytes("col3"), Bytes.toBytes("value03"));

			Put put02 = new Put(Bytes.toBytes("row03"));// 参数是行健row01
			put02.addColumn(Bytes.toBytes("fm1"), Bytes.toBytes("col4"),
					Bytes.toBytes("value04"));

			List<Put> puts = Arrays.asList(put01, put02);

			// 获得表对象
			Table table02 = con.getTable(tn);
			table02.put(puts);

			// 读取操作
			// scan
			Scan scan = new Scan();
			// 获得表对象
			Table table03 = con.getTable(tn);
			// 得到扫描的结果集
			ResultScanner rs = table03.getScanner(scan);
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

					System.out.println("1 ===> rowKey : " + rowKey
							+ ",  timestamp : " + timestamp + ", family : "
							+ family + ", qualifier : " + qualifier
							+ ", value : " + value);
				}
			}
			System.out.println(" ===================get取数据==================");

			// get
			Get get = new Get(Bytes.toBytes("row02"));
			get.addColumn(Bytes.toBytes("fm2"), Bytes.toBytes("col2"));
			Table t04 = con.getTable(tn);
			Result r = t04.get(get);
			List<Cell> cs = r.listCells();
			for (Cell cell : cs) {
				String rowKey = Bytes.toString(CellUtil.cloneRow(cell)); // 取行键
				long timestamp = cell.getTimestamp(); // 取到时间戳
				String family = Bytes.toString(CellUtil.cloneFamily(cell)); // 取到族列
				String qualifier = Bytes
						.toString(CellUtil.cloneQualifier(cell)); // 取到修饰名
				String value = Bytes.toString(CellUtil.cloneValue(cell)); // 取到值

				System.out.println("2 ===> rowKey : " + rowKey
						+ ",  timestamp : " + timestamp + ", family : "
						+ family + ", qualifier : " + qualifier + ", value : "
						+ value);
			}

			// 删除数据
			System.out.println(" ===================delete删除数据==================");
			Delete delete = new Delete(Bytes.toBytes("row02"));
			delete.addColumn(Bytes.toBytes("fm2"), Bytes.toBytes("col2"));
			Table t05 = con.getTable(tn);
			t05.delete(delete);

			System.out.println(" ===================delete删除数据后==================");
			// scan
			scan = new Scan();
			table03 = con.getTable(tn); // 获得表对象
			rs = table03.getScanner(scan);
			for (Result result : rs) {
				cs = result.listCells();
				for (Cell cell : cs) {
					String rowKey = Bytes.toString(CellUtil.cloneRow(cell)); // 取行键
					long timestamp = cell.getTimestamp(); // 取到时间戳
					String family = Bytes.toString(CellUtil.cloneFamily(cell)); // 取到族列
					String qualifier = Bytes.toString(CellUtil
							.cloneQualifier(cell)); // 取到修饰名
					String value = Bytes.toString(CellUtil.cloneValue(cell)); // 取到值

					System.out.println("3 ===> rowKey : " + rowKey
							+ ",  timestamp : " + timestamp + ", family : "
							+ family + ", qualifier : " + qualifier
							+ ", value : " + value);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// 5.关闭

		try {
			if (admin != null) {
				admin.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}