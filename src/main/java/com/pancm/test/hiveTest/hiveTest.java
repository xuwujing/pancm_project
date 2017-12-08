package com.pancm.test.hiveTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;
    //创建连接
    static {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            connection = DriverManager.getConnection("jdbc:hive2://192.169.0.23:10000", "root", "123456");
            System.out.println(connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    //关闭连接
    public static void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
           System.out.println("连接关闭！");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    //查询
    public static void find(String tableName) throws SQLException {
    	String sql = "select * from "+ tableName ;
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        List list=convertList(rs);
        System.out.println("查询结果:"+list);
    }
    
    //查询
    public static void find1(String sql) throws SQLException {
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        List list=convertList(rs);
        System.out.println("查询结果:"+list);
    }
    
	/**
	 * 将查询的数据转换成List类型
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List convertList(ResultSet rs) throws SQLException {
        if(null==rs){
        	return null;
        }
		List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list; 
	}
	
   
    
    public static void main(String[] args) throws SQLException {
    	  String tableName = "t_student";
    	  String tableName1= "t_student_info";
    	  String sql=" select * from t_student t , t_student_info ti where t.key=ti.key and t.key=1001 ";
    	  long starTime=System.currentTimeMillis(); 
//    	  find(tableName);   	  
//    	  find(tableName1);
    	  find1(sql);
    	  long endTime=System.currentTimeMillis(); 
    	  System.out.println("查询共使用时间为:"+(endTime-starTime)+"毫秒");
	}
    
  
  
 
     
}
