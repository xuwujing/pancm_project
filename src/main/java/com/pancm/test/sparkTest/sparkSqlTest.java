package com.pancm.test.sparkTest;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import scala.Tuple2;

/**
 * Title: sparkSqlTest
 * Description: SparkOnHbase 的测试
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017年11月24日
 */
public class sparkSqlTest {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
    	System.out.println("开始...");
//        System.setProperty("hadoop.home.dir", "E:\\hadoop");
//        System.setProperty("HADOOP_USER_NAME", "root"); 
//        System.setProperty("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        SparkSession spark=SparkSession.builder()  
                .appName("lcc_java_read_hbase_register_to_table")  
                .master("local[*]")  
                .getOrCreate();  

        JavaSparkContext context = new JavaSparkContext(spark.sparkContext());

        Configuration conf = HBaseConfiguration.create();  
        conf.set("hbase.zookeeper.property.clientPort", "2181");  
        conf.set("hbase.zookeeper.quorum", "192.169.0.25");  



        Scan scan = new Scan();
        String tableName = "t_student";
        conf.set(TableInputFormat.INPUT_TABLE, tableName);

        org.apache.hadoop.hbase.protobuf.generated.ClientProtos.Scan proto = ProtobufUtil.toScan(scan);
        String ScanToString = Base64.encodeBytes(proto.toByteArray());
        conf.set(TableInputFormat.SCAN, ScanToString);

        JavaPairRDD<ImmutableBytesWritable, Result> myRDD = context.newAPIHadoopRDD(conf,TableInputFormat.class, ImmutableBytesWritable.class, Result.class);

        JavaRDD<Row> personsRDD = myRDD.map(new Function<Tuple2<ImmutableBytesWritable,Result>,Row>() {

            @Override
            public Row call(Tuple2<ImmutableBytesWritable, Result> tuple) throws Exception {
                // TODO Auto-generated method stub
                System.out.println("====tuple=========="+tuple);
                Result result = tuple._2();
                String rowkey = Bytes.toString(result.getRow());
                String name = Bytes.toString(result.getValue(Bytes.toBytes("lcc_liezu"), Bytes.toBytes("name")));
                String sex = Bytes.toString(result.getValue(Bytes.toBytes("lcc_liezu"), Bytes.toBytes("sex")));
                String age = Bytes.toString(result.getValue(Bytes.toBytes("lcc_liezu"), Bytes.toBytes("age")));

                //这一点可以直接转化为row类型
                return (Row) RowFactory.create(rowkey,name,sex,age);
            }

        });

        List<StructField> structFields=new ArrayList<StructField>();  
        structFields.add(DataTypes.createStructField("id", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("name", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("sex", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("age", DataTypes.StringType, true));

        StructType schema=DataTypes.createStructType(structFields);  

        Dataset stuDf=spark.createDataFrame(personsRDD, schema);
       //stuDf.select("id","name","age").write().mode(SaveMode.Append).parquet("par");  
        stuDf.printSchema();  
        stuDf.createOrReplaceTempView("Person");  
        Dataset<Row> nameDf=spark.sql("select * from Person ");  
        nameDf.show();  

    }

}