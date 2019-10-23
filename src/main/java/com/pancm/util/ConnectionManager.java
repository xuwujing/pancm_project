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
 * The type Connection manager.
 *
 * @author pancm
 * @Title: ConnectionManager
 * @Description:数据库连接初始类
 * @Version:1.0.0
 * @date 2018年1月4日
 */
public class ConnectionManager {
	private static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	/** 获取jdbc配置文件参数 */
	private static Map<String, String> conf = GetProperties.getAppSettings();

	private static volatile ConnectionManager dbConnection;

	//  连接池
	private DruidDataSource dataSource = null;

	private String msg = "";
	
	
	/** 密码的key */
	private final String sysKey = "password";

	

	/** 配置文件名称 */
	private final String fileName = "jdbc.properties";
	/**
	 * 在构造函数初始化的时候获取数据库连接
	 */
	private ConnectionManager() {
		if (dataSource == null) {
			/** 获取属性文件中的值 **/
			String driverName = conf.get("driverClassName");
			String url = conf.get("url");
			String username = conf.get("username");
			String password = conf.get("password");
			int maxPoolSize = 100;
		
			dataSource = new DruidDataSource();
			dataSource.setName("_pool");
			dataSource.setUrl(url);
			// 数据库驱动
			dataSource.setDriverClassName(driverName);
			dataSource.setUsername(username);// 用户名
			String	password2 = AESEncryptDecrypt.decode(password);
			// 如果为空,说明解密失败,那么就直接连接，若还是失败，说明密码错误，直接退出程序
			dataSource.setPassword(password2 == null ? password : password2);

			// 如果连接失败，则直接退出程序!
			if (!testCon(dataSource)) {
				logger.error("数据库连接失败!程序退出!");
				System.exit(1);
			}
			
			// 连接成功并且是明文的话,就直接回写密文
			if (password2 == null) {
				logger.info("从数据库的密码是明文!");
				SetSystemProperty ssp = new SetSystemProperty();
				Map<String, String> map = new HashMap<String, String>();
				map.put(sysKey, AESEncryptDecrypt.encode(password));
				ssp.updateProperties(fileName, map, "password Encode");
				logger.info("从数据库的密文回写成功!");
			}else{
				logger.info("从数据库的密码是密文!");
			}
			dataSource.setMaxActive(maxPoolSize);// 连接池中保留的最大连接数
//			dataSource.setInitialSize(1);// 连初始创建

			dataSource.setTimeBetweenEvictionRunsMillis(60000);
			dataSource.setMaxWait(30000);
			// 每次连接不校验有效性，提升性能
			dataSource.setTestOnBorrow(false);
			dataSource.setTestOnReturn(false);

		}

	}

    /**
     * 连接测试
     *
     * @param dataSource the data source
     * @return boolean
     */
    public boolean testCon(DataSource dataSource) {
		try {
			dataSource.getConnection();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

    /**
     * 获取数据库连接对象，单例
     *
     * @return instance
     */
    public static ConnectionManager getInstance() {
		if (dbConnection == null) {
			synchronized (ConnectionManager.class) {
				if (dbConnection == null) {
					dbConnection = new ConnectionManager();
				}
			}
		}
		return dbConnection;
	}

    /**
     * Gets connection.
     *
     * @return the connection
     * @throws SQLException the sql exception
     */
    public final Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			msg = "数据库连接失败!";
			throw new SQLException(msg);
		}
		return conn;
	}

    /**
     * Close.
     *
     * @param rs         the rs
     * @param stmt       the stmt
     * @param connection the connection
     */
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
