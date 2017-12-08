package com.pancm.test.jdbcTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MakeTestDataUtil {

	private static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	private static String dbURL = "jdbc:sqlserver://192.169.2.204:1433;DatabaseName=testmsg";

	private static String userName = "sa";

	private static String userPwd = "123zxcvbnm,./";

	public static void main(String[] args) throws SQLException {
		test();

	}
	
	private static void test() throws SQLException{
		try {
			String insertSql=" INSERT into test_msg (id,msg,msg1,msg2) values (10,'abc\\ "
					+ "qwe','jk"
					+ "\\ kl','qwe \\"
					+ "qqq');";
			String sql = "select msg from test_msg where id = 3";
			Connection conn = getCoonection();
			Statement stmt =conn.createStatement();
			stmt.executeUpdate(insertSql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			String msg  = null;
			while(rs.next()){
				msg = rs.getString("msg");
				break;
			}
			String sql2 = "select * from test_msg where msg=("+sql+")";
			
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ResultSet rs2 = ps2.executeQuery();
			String msg2  = null;
			while(rs2.next()){
				msg2 = rs.getString("msg");
				break;
			}
			System.out.println(msg2);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
		}
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
