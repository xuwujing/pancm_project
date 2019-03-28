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

/**
 * The type Connection oracle.
 */
public class ConnectionOracle {
    /**
     * The Sd.
     *
     * @param args
     */

    String sd = "oracle.jdbc.driver.OracleDriver";

    /**
     * The Sc.
     */
//连接地址
	String sc = "jdbc:oracle:thin:@172.26.129.14:1521:ORCL";

    /**
     * The User name.
     */
    String userName = "pos_scm";

    /**
     * The Password.
     */
    String password = "jlpos";

	// String sd="com.mysql.jdbc.Driver";

	// String
	// sc="jdbc:mysql://localhost:3306/payManagerDB?useUnicode=true&amp;characterEncoding=utf8";

    /**
     * The Con.
     */
    Connection con = null;

    /**
     * The Stmt.
     */
    Statement stmt = null;

    /**
     * The Rs.
     */
    ResultSet rs = null;

    /**
     * Instantiates a new Connection oracle.
     */
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

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

		// TODO Auto-generated method stub

	}

    /**
     * Execute query list.
     *
     * @param sql the sql
     * @return the list
     * @throws SQLException the sql exception
     */
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

    /**
     * Execute update.
     *
     * @param sql the sql
     * @throws SQLException the sql exception
     */
    public void executeUpdate(String sql) throws SQLException

	{

		con = DriverManager.getConnection(sc, userName, password);

		Statement stmt = con.createStatement();

		stmt.executeUpdate(sql);

	}

    /**
     * Close.
     *
     * @throws SQLException the sql exception
     */
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