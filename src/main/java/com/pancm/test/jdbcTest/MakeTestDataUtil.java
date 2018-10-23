package com.pancm.test.jdbcTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MakeTestDataUtil {

	private static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	private static String dbURL = "jdbc:sqlserver://192.169.2.204:1433;DatabaseName=pancm_DB";

	private static String userName = "sa";

	private static String userPwd = "123zxcvbnm,./";

	public static void main(String[] args) throws SQLException {
		System.out.println("开始");
		long start=System.currentTimeMillis();
		test();
		System.out.println("使用时间:"+(System.currentTimeMillis()-start)+"ms");
	}
	
	private static void test() throws SQLException{
		String insertSql="";
		List<String> list=new ArrayList<String>();
		long phone=12345678912L;
		for(int i=0;i<2;i++){
			long qw=phone+i;
			 insertSql=" INSERT INTO PB_VALID_PHONE ( PHONE, STATUS, "
					+ "CREATETM, RMSSTATUS,  LASTDLTM, RMSFG, PHONEOS, "
					+ "OSVER, BRAND, MODEL, XBROWSER, IMEI, IMSI, ISCHANGE) "
					+ "VALUES ('"+ qw +"', '1', '2018-03-12 09:49:47.0000000', "
					+ "'3',  '2018-03-12 09:49:47.0000000', "
					+ "'1', '1', 'ios6.0', '苹果', 'iphone6', 'IE8', '345', '133', '2');";
			 list.add(insertSql);
		}
		updateBatch(list);
	}

	public static int[] updateBatch(List<String> sqls) throws SQLException{
		Statement stmt = null;
		Connection connection = getCoonection();
		int[] t={0,0};
		try {
		   stmt = connection.createStatement();
			  /**设置不自动提交，以便于在出现异常的时候数据库回滚**/
           connection.setAutoCommit(false);
			for (int i = 0, j = sqls.size(); i < j; i++) {
				stmt.addBatch(sqls.get(i));
			}
			t=stmt.executeBatch();
		    connection.commit();
		} catch (Exception e) {
			if (connection != null) {
                try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
			e.printStackTrace();
		} finally {
		}
		return t;
	}
	
	

	private static Connection getCoonection() {
		try {
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(dbURL, userName, userPwd);
			return conn;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.print("----------------连接失败");
		}
		return null;
	}

	public static ResultSet executeQuery(String SQL) {
		try {
			Connection conn = getCoonection();
			System.out.println("---------------连接数据库成功");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("----------------查询失败");
		}
		return null;
	}
}
