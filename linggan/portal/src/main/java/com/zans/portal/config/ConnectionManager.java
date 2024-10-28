package com.zans.portal.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.zans.portal.config.GlobalConstants.dataSourceConcurrentMap;

/**
 * @author pancm
 * @Title: ConnectionManager
 * @Description:数据库连接初始类
 * @Version:1.0.0
 * @date 2018年1月4日
 */
public class ConnectionManager {
    private static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);



    DruidDataSource druidDataSource;

    public static String DRIVER= "com.mysql.jdbc.Driver";


    /**
     * 在构造函数初始化的时候获取数据库连接
     */
    public ConnectionManager(String name, String url,String username,String password) {

        druidDataSource = new DruidDataSource();
        druidDataSource.setName("t_pool");
        druidDataSource.setDriverClassName(DRIVER);
        druidDataSource.setUrl(url);
        // 用户名
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        // 如果为空,说明解密失败,那么就直接连接，若还是失败，说明密码错误，直接退出程序
        druidDataSource.setPassword(password);

        // 如果连接失败，则直接退出程序!
        if (!testCon(druidDataSource)) {
            logger.error("数据库连接失败!程序退出!");
            System.exit(1);
        }
        // 连接池中保留的最大连接数
        druidDataSource.setMaxActive(10);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMaxWait(10000);
        // 每次连接不校验有效性，提升性能
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        dataSourceConcurrentMap.put(name, druidDataSource);
        logger.info("{} 数据库连接初始化成功！", name);
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
