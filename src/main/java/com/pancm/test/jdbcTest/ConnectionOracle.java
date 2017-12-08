package com.pancm.test.jdbcTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionOracle {
	/**
	 * 
	 * @param args
	 */

	String sd = "oracle.jdbc.driver.OracleDriver";

    //连接地址
	String sc = "jdbc:oracle:thin:@172.26.129.14:1521:ORCL";
	
	String userName = "pos_scm";

	String password = "jlpos";

	// String sd="com.mysql.jdbc.Driver";

	// String
	// sc="jdbc:mysql://localhost:3306/payManagerDB?useUnicode=true&amp;characterEncoding=utf8";

	Connection con = null;

	Statement stmt = null;

	ResultSet rs = null;

	public ConnectionOracle()

	{

		try

		{
            //������
			Class.forName(sd).newInstance();

		}

		catch (Exception e)

		{

			System.err.println(e.getMessage());

		}

	}

	public static void main(String[] args) {

		// TODO Auto-generated method stub

	}

	public List executeQuery(String sql) throws SQLException

	{

		con = DriverManager.getConnection(sc, userName, password);

		Statement stmt = con.createStatement();

		rs = stmt.executeQuery(sql);
		
	    List cc =convertList(rs);
        System.out.println(cc);
		return cc;

	}
	private static List convertList(ResultSet rs) throws SQLException {
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

	public void executeUpdate(String sql) throws SQLException

	{

		con = DriverManager.getConnection(sc, userName, password);

		Statement stmt = con.createStatement();

		stmt.executeUpdate(sql);

	}

	public void close() throws SQLException

	{

		if (rs != null) {
			rs.close();
		}

		if (stmt != null) {
			stmt.close();
		}

		if (con != null) {
			con.close();
		}

	}

}