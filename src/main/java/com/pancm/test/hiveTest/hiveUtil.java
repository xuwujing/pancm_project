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
		if (null == connection) {
			try {
			    connection = DriverManager.getConnection("jdbc:hive2://192.169.0.23:10000", "root", "123456");
			} catch (SQLException e) {
				System.out.println("获取数据库连接失败！");
				 e.printStackTrace();
			}
		}
		return connection;
	}

	public synchronized static boolean closeConnetion() {
		try {
			if (null != connection) {
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("关闭数据库连接失败！");
			e.printStackTrace();
		}
		return true;
	}
	
	public static void select(String tableName){
		String str="select * from "+tableName+";";
		ResultSet rs=null;
		try {
			if(stmt==null||stmt.isClosed()){
				stmt=getConnection().createStatement();
			}
			rs=stmt.executeQuery(str);
		   List list = convertList(rs);
		   System.out.println("数据库总计数据:"+list.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close();
		}
	}
	
	 //查询
    public static void find(String sql) throws SQLException {
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getObject(1) + "---" + rs.getObject(2));
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
	           System.out.println("连接关闭！");
	        } catch (SQLException e) {
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
	

}
