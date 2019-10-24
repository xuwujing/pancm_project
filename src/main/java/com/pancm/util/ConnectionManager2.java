package com.pancm.util;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Title: ConnectionManager
 * @Description:数据库连接初始类
 * @Version:1.0.0
 * @author pancm
 * @date 2018年1月4日
 */
public class ConnectionManager2 {
	private static Logger logger = LoggerFactory.getLogger(ConnectionManager2.class);
	/** 获取jdbc配置文件参数 */
	private static Map<String, String> conf = GetProperties.getAppSettings();

	private String msg = "";
	private final String fileName = "jdbc.properties";
	private String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private String driverClassName2 = "com.mysql.jdbc.Driver";
	private String urlName="sql.url";
	private String dbName="sql.db";
	private String usernameName="sql.username";
	private String pwdName="sql.password";
	/**
	 * 在构造函数初始化的时候获取数据库连接
	 */
	public ConnectionManager2(int i) {
			DruidDataSource druidDataSource = null;
			/** 获取属性文件中的值 **/
			String url = conf.get(urlName+i);
			String db = conf.get(dbName+i);
			String username = conf.get(usernameName+i);
			String password = conf.get(pwdName+i);

			int  dbType =1;
			try{
				dbType = Integer.parseInt(conf.get("DBType"));
			}catch (Exception e){
				logger.error("数据库方式配置不正确！应该为1或2！请检查配置！程序退出!",e);
				System.exit(0);
			}

			druidDataSource = new DruidDataSource();
			druidDataSource.setName("t_pool"+i);
			// 数据库驱动
			if (dbType == 1) {
				logger.info("第{}编号连接的数据库是SQLServer",i);
				url = "jdbc:sqlserver://"+url+";DatabaseName="+db;
				druidDataSource.setDriverClassName(driverClassName);
			} else {
				logger.info("第{}编号连接的数据库是MySql",i);
				url = "jdbc:mysql://"+url+"/"+db+"?useUnicode=true&characterEncoding=utf8";
				druidDataSource.setDriverClassName(driverClassName2);
			}
			druidDataSource.setUrl(url);
			// 用户名
			druidDataSource.setUsername(username);
			druidDataSource.setPassword(password);

			String	password2 = AESEncryptDecrypt.decode(password);
			// 如果为空,说明解密失败,那么就直接连接，若还是失败，说明密码错误，直接退出程序
			druidDataSource.setPassword(password2 == null ? password : password2);

			// 如果连接失败，则直接退出程序!
			if (!testCon(druidDataSource)) {
				logger.error("第{}编号的数据库连接失败!程序退出!",i);
				System.exit(1);
			}

			// 连接成功并且是明文的话,就直接回写密文
			if (password2 == null) {
				logger.info("第{}编号的数据库的密码是明文!",i);
				SetSystemProperty ssp = new SetSystemProperty();
				Map<String, String> map = new HashMap<String, String>();
				map.put(pwdName+i, AESEncryptDecrypt.encode(password));
				ssp.updateProperties(fileName, map, null);
				logger.info("第{}编号的数据库的密文回写成功!",i);
			}else{
				logger.info("第{}编号的数据库的密码是密文!",i);
			}
			// 连接池中保留的最大连接数
			druidDataSource.setMaxActive(10);
//			ec_pool.setInitialSize(2);// 连初始创建
			druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
			druidDataSource.setMaxWait(30000);
			// 每次连接不校验有效性，提升性能
			druidDataSource.setTestOnBorrow(false);
			druidDataSource.setTestOnReturn(false);
		Constans.dataSourceConcurrentMap.put(i,druidDataSource);

		    logger.info("第{}编号的数据库初始化成功!",i);
	}

	/**
	 * 连接测试
	 * 
	 * @param dataSource
	 * @param
	 * @param
	 * @return
	 */
	public boolean testCon(DataSource dataSource) {
		try {
			dataSource.getConnection();
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	public final Connection getConnection(int i) throws SQLException {
		Connection conn = null;
		try {
			conn = Constans.dataSourceConcurrentMap.get(i).getConnection();
		} catch (SQLException e) {
			msg = "第"+i+"编号的数据库连接失败!请检查对应数据库连接配置！";
			throw new SQLException(msg);
		}
		return conn;
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
