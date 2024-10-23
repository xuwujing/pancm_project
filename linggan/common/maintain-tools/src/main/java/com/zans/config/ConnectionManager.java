package com.zans.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zans.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static com.zans.config.Constants.*;

/**
 * @author pancm
 * @Title: ConnectionManager
 * @Description:数据库连接初始类
 * @Version:1.0.0
 * @date 2018年1月4日
 */
public class ConnectionManager {
    private static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private String msg = "";


    /**
     * 在构造函数初始化的时候获取数据库连接
     */
    public ConnectionManager(Map<String, String> conf,int i) {
        DruidDataSource druidDataSource = null;
        /** 获取属性文件中的值 **/
        String url = conf.get(URL_NAME + i);
        String db = conf.get(DB_NAME + i);
        String username = conf.get(USERNAME + i);
        String password = conf.get(PWD_NAME + i);
        int port = Integer.parseInt(conf.getOrDefault(PORT + i, "3306"));
        Constants.dataSourceName.put(i, db);

        druidDataSource = new DruidDataSource();
        druidDataSource.setName("t_pool" + i);

        logger.info("第{}编号连接的数据库是MySql", i);
        url = "jdbc:mysql://" + url+":"+port + "/" + db + "?useUnicode=true&characterEncoding=utf8";
        druidDataSource.setDriverClassName(DRIVER_CLASS_NAME);
        druidDataSource.setUrl(url);
        // 用户名
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        String password2 = AESUtil.desEncrypt(password).trim();
		// 连接成功并且是明文的话,就直接回写密文
		if (password2 == null) {
			logger.warn("第{}编号的数据库的密码是明文!", i);
		}
		// 如果为空,说明解密失败,那么就直接连接，若还是失败，说明密码错误，直接退出程序
        druidDataSource.setPassword(password2 == null ? password : password2);

        // 如果连接失败，则直接退出程序!
        if (!testCon(druidDataSource)) {
            logger.error("第{}编号的数据库连接失败!程序退出!", i);
            System.exit(1);
        }
        conf.put(PWD_NAME+i,password2);

        // 连接池中保留的最大连接数
        druidDataSource.setMaxActive(1);
//			ec_pool.setInitialSize(2);// 连初始创建
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMaxWait(30000);
        // 每次连接不校验有效性，提升性能
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        Constants.dataSourceConcurrentMap.put(i, druidDataSource);
        Constants.dataSourceStatus.put(i, true);
        logger.info("第{}编号的数据库初始化成功!", i);
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
            conn = Constants.dataSourceConcurrentMap.get(i).getConnection();
        } catch (SQLException e) {
            msg = "第" + i + "编号的数据库连接失败!请检查对应数据库连接配置！";
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
