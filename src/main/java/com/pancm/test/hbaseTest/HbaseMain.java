package com.pancm.test.hbaseTest;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseMain {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.169.0.23:16010");

        // 1.创建表
        String tableName = "t1";
        String[] family = { "cf1", "cf2" };
        // creatTable(tableName, family,conf);

        // 2.为表添加数据
        String[] column1 = { "cf1test1", "cf1test2", "cf1test3" };
        String[] value1 = { "one", "two", "three" };
        String[] column2 = { "cf2test1", "cf2test2" };
        String[] value2 = { "lijie", "chongqing" };
         addData("rowkey1", "t1", "cf1", column1, value1, "cf2", column2,
         value2, conf);
         addData("rowkey2", "t1", "cf1", column1, value1, "cf2", column2,
         value2, conf);
         addData("rowkey3", "t1", "cf1", column1, value1, "cf2", column2,
         value2, conf);

        // 3.rowkey查询
         getResult("t1", "rowkey1", conf);

        // 4.遍历表
         getScann("t1", conf);

        // 5.rowkey范围查询
         getScannRange("t1", "rowkey1", "rowkey1", conf);

        // 6.查询具体的某一列
         getColumn("t1", "rowkey1", "cf1", "cf1test1", conf);

        // 7.更新数据
         updateColumn("t1", "rowkey1", "cf1", "cf1test1", "lijietest", conf);

        // 8.多版本查询
         getMulVersion("t1", "rowkey1", "cf1", "cf1test1", conf);

        // 9.删除指定列
         deleteColumn("t1", "rowkey1", "cf1", "cf1test1", conf);

        // 10.删除一行数据
         deleteAllColumn("t1", "rowkey1", conf);

        // 11.删除表
         deleteTable("t1", conf);
    }

    /**
     * 创建表
     * 
     * @param tableName 表名字
     * @param family 列族
     * @param conf
     * @throws Exception
     */
    public static void creatTable(String tableName, String[] family,
            Configuration conf) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        HTableDescriptor desc = new HTableDescriptor(tableName);
        for (int i = 0; i < family.length; i++) {
            desc.addFamily(new HColumnDescriptor(family[i]));
        }
        if (admin.tableExists(tableName)) {
            System.out.println("表存在!");
        } else {
            admin.createTable(desc);
            System.out.println("创建成功!");
        }

    }

    /**
     * 插入数据
     * 
     * @param rowKey
     * @param tableName  表名字
     * @param cf1 列族1
     * @param column1 列族1的列限定符
     * @param value1 列族1的值
     * @param cf2 列族2
     * @param column2 列族2的列限定符
     * @param value2 列族2的值
     * @param conf
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void addData(String rowKey, String tableName, String cf1,
            String[] column1, String[] value1, String cf2, String[] column2,
            String[] value2, Configuration conf) throws IOException {
        Put put = new Put(Bytes.toBytes(rowKey));
        for (int i = 0; i < column1.length; i++) {
            put.add(Bytes.toBytes(cf1), Bytes.toBytes(column1[i]),
                    Bytes.toBytes(value1[i]));
        }

        for (int i = 0; i < column2.length; i++) {
            put.add(Bytes.toBytes(cf2), Bytes.toBytes(column2[i]),
                    Bytes.toBytes(value2[i]));
        }
        HTable table = new HTable(conf, tableName);
        table.put(put);
        System.out.println("数据插入成功！");
    }

    /**
     * 根据rowkey查询
     * 
     * @param tableName  表名字
     * @param rowKey
     * @param conf
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void getResult(String tableName, String rowKey,
            Configuration conf) throws IOException {
        Get get = new Get(Bytes.toBytes(rowKey));
        HTable table = new HTable(conf, tableName);
        Result result = table.get(get);
        List<KeyValue> list = result.list();
        for (KeyValue kv : list) {
            System.out.println("列族:" + Bytes.toString(kv.getFamily()));
            System.out.println("列族限定名:" + Bytes.toString(kv.getQualifier()));
            System.out.println("值:" + Bytes.toString(kv.getValue()));
            System.out.println("时间戳:" + kv.getTimestamp());
        }
    }

    /**
     * 遍历查询hbase表
     * 
     * @param tableName 表名字
     * @param conf
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void getScann(String tableName, Configuration conf)
            throws IOException {
        Scan scan = new Scan();
        HTable table = new HTable(conf, tableName);
        ResultScanner scanner = null;
        try {
            scanner = table.getScanner(scan);
            for (Result result : scanner) {
                List<KeyValue> list = result.list();
                for (KeyValue kv : list) {
                    System.out.println("rowkey:" + Bytes.toString(kv.getRow()));
                    System.out.println("列族:" + Bytes.toString(kv.getFamily()));
                    System.out.println("列族限定名:"
                            + Bytes.toString(kv.getQualifier()));
                    System.out.println("值:" + Bytes.toString(kv.getValue()));
                    System.out.println("时间戳:" + kv.getTimestamp());
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            scanner.close();
        }

    }

    /**
     * rowkey范围查询
     * 
     * @param tableName 表名字
     * @param start_rowkey 开始的rowkey
     * @param stop_rowkey 结束的rowkey
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void getScannRange(String tableName, String start_rowkey,
            String stop_rowkey, Configuration conf) throws IOException {
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(start_rowkey));
        scan.setStopRow(Bytes.toBytes(stop_rowkey));
        ResultScanner rs = null;
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        try {
            rs = table.getScanner(scan);
            for (Result r : rs) {
                for (KeyValue kv : r.list()) {
                    System.out.println("rowkey:" + Bytes.toString(kv.getRow()));
                    System.out.println("列族:" + Bytes.toString(kv.getFamily()));
                    System.out.println("列族限定名:"
                            + Bytes.toString(kv.getQualifier()));
                    System.out.println("值:" + Bytes.toString(kv.getValue()));
                    System.out.println("时间戳:" + kv.getTimestamp());
                }
            }
        } finally {
            rs.close();
        }
    }

    /**
     * 查询某一列
     * 
     * @param tableName  表名字
     * @param rowKey
     * @param familyName 列族
     * @param columnName  列限定符
     * @param conf
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void getColumn(String tableName, String rowKey,
            String familyName, String columnName, Configuration conf)
            throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        Result result = table.get(get);
        for (KeyValue kv : result.list()) {
            System.out.println("列族:" + Bytes.toString(kv.getFamily()));
            System.out.println("列族限定名:" + Bytes.toString(kv.getQualifier()));
            System.out.println("值:" + Bytes.toString(kv.getValue()));
            System.out.println("时间戳:" + kv.getTimestamp());
        }
    }

    /**
     * 更新表的列
     * 
     * @param tableName 表名字
     * @param rowKey
     * @param familyName 列族
     * @param columnName 列限定符
     * @param value 值
     * @param conf
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void updateColumn(String tableName, String rowKey,
            String familyName, String columnName, String value,
            Configuration conf) throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(familyName), Bytes.toBytes(columnName),
                Bytes.toBytes(value));
        table.put(put);
        System.out.println("更新成功");
    }

    /**
     * 多版本查询
     * 
     * @param tableName  表名字
     * @param rowKey
     * @param familyName 列族
     * @param columnName  列限定符
     * @param conf
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void getMulVersion(String tableName, String rowKey,
            String familyName, String columnName, Configuration conf)
            throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        get.setMaxVersions(5);
        List<KeyValue> list = table.get(get).list();
        for (KeyValue kv : list) {
            System.out.println("列族:" + Bytes.toString(kv.getFamily()));
            System.out.println("列族限定名:" + Bytes.toString(kv.getQualifier()));
            System.out.println("值:" + Bytes.toString(kv.getValue()));
            System.out.println("时间戳:" + kv.getTimestamp());
        }
    }

    /**
     * 删除指定的列
     * 
     * @param tableName 表名字
     * @param rowKey
     * @param familyName 列族
     * @param columnName 列限定符
     * @param conf
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void deleteColumn(String tableName, String rowKey,
            String familyName, String columnName, Configuration conf)
            throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.deleteColumn(Bytes.toBytes(familyName),
                Bytes.toBytes(columnName));
        table.delete(delete);
        System.out.println("删除了" + columnName + ",删除成功！");
    }

    /**
     * 删除一行数据
     * 
     * @param tableName 表名字
     * @param rowKey
     * @param conf
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void deleteAllColumn(String tableName, String rowKey,
            Configuration conf) throws IOException {
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Delete deleteAll = new Delete(Bytes.toBytes(rowKey));
        table.delete(deleteAll);
        System.out.println("删除了一行数据，rowkey为；" + rowKey + ",删除成功！");
    }

    /**
     * 删除表
     * 
     * @param tableName 表名字
     * @param conf
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public static void deleteTable(String tableName, Configuration conf)
            throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
        System.out.println("删除表:" + tableName + ",操作成功！");
    }

}
