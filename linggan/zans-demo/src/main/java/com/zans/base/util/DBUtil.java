package com.zans.base.util;

import com.zans.base.config.GlobalConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Title: DBUtil
 * @Description: 数据库JDBC连接工具类
 * @Version:1.0.0
 * @author pancm
 * @date 2018年1月5日
 */
public class DBUtil {

	private static Logger logger = LoggerFactory.getLogger(DBUtil.class);

	private  String name;
	public DBUtil(String name){
		this.name = name;
	}

	private  Connection getConnection() throws SQLException {
		return GlobalConstants.dataSourceConcurrentMap.get(name).getConnection();
	}

	/**
	 * 查询
	 *
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public  List<Map<String, Object>> executeQuery(String sql) throws SQLException {
		Statement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = null;
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			list = convertList(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs, stmt, connection);
		}
		return list;
	}

	/**
	 * 将查询的数据转换成List类型
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private  List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {
		if (null == rs) {
			return null;
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}


	public static void close(ResultSet rs, Statement stmt, Connection connection) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.error("数据连接关闭失败！", e);
		}
	}


}
