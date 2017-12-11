package com.pancm.test.hiveTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class hiveUtil {
	
    private static Connection connection;
    private static PreparedStatement ps;
    private static ResultSet rs;
    private static Statement stmt; 
	/***
	 * 静态加载Sqlite驱动类
	 */
	static {
		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("加载驱动失败！");
			e.printStackTrace();
		}
	}

	public synchronized static Connection getConnection() {
		try {
		if (null == connection||connection.isClosed()) {
			    connection = DriverManager.getConnection("jdbc:hive2://192.169.0.23:10000", "root", "123456");
			}
		}catch (SQLException e) {
				System.out.println("获取数据库连接失败！");
				 e.printStackTrace();
			}
		
		return connection;
	}

	
	
	
	/**
	 * 通过sql进行查询
	 * @param sql
	 */
    public static void find(String sql) {
    	try {
			if(null==connection||connection.isClosed()){
				stmt=getConnection().createStatement();
				//设置使用spark引擎
//				 boolean resHivePropertyTest = stmt
//				            .execute("set hive.execution.engine=mr");
//				  System.out.println(resHivePropertyTest);
			}
			rs=stmt.executeQuery(sql);
	        List list=convertList(rs);
	        System.out.println("查询结果:"+list);
		} catch (SQLException e) {
			System.out.println("查询失败");
			e.printStackTrace();
		}finally{
			close();
		}      
    }
    

	
	
	   public static void close() {
	        try {
	            if (rs != null) {
	                rs.close();
	            }
	            if (ps != null) {
	                ps.close();
	            }
	            if(stmt!=null){
	            	stmt.close();
	            }
	            if (connection != null) {
	                connection.close();
	            }
	        } catch (SQLException e) {
	        	 System.out.println("数据库连接关闭失败！");
	            e.printStackTrace();
	            System.exit(1);
	        }
	    }
	
	
	/**
	 * 将查询的数据转换成List类型
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List convertList(ResultSet rs) throws SQLException {
        if(null==rs||rs.isClosed()){
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
	

}
