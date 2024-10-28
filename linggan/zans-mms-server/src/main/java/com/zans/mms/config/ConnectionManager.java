package com.zans.mms.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zans.base.exception.BusinessException;
import com.zans.mms.dao.SysConstantItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:druid数据库连接池配置
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/11
 */
@Slf4j
public class ConnectionManager {
	DruidDataSource druidDataSource;

	@Value("${druid.username}")
	private String username;

	@Value("${druid.password}")
	private String password;

	@Value("${druid.url}")
	private String url;

	@Autowired
	SysConstantItemMapper sysConstantItemMapper;


	/**
	 * 在构造函数初始化的时候获取数据库连接
	 */
	public  ConnectionManager() {
		/** 获取属性文件中的值 **/
		String dc ="oracle.jdbc.OracleDriver";
		druidDataSource = new DruidDataSource();
		druidDataSource.setName("t_pool");
		druidDataSource.setDriverClassName(dc);
		if(StringUtils.isBlank(url)){
			url="jdbc:oracle:thin:@27.10.21.10:10036/otdb";
		}
		if(StringUtils.isBlank(username)){
			username="chb_lg";
		}
		if(StringUtils.isBlank(password)){
			username="lg#2021";
		}
		druidDataSource.setUrl(url);
		// 用户名
		druidDataSource.setUsername(username);
		druidDataSource.setPassword(password);
		log.info("url:{},username:{},password:{}",url,username,password);
		// 如果为空,说明解密失败,那么就直接连接，若还是失败，说明密码错误，直接退出程序
//        String	password2 = AESUtil.desEncrypt(password,AES_KEY,AES_KEY);
//        // 如果为空,说明解密失败,那么就直接连接，若还是失败，说明密码错误，直接退出程序
//        druidDataSource.setPassword(password2 == null ? password : password2);
//        druidDataSource.setPassword(password);

		// 如果连接失败，则直接退出程序!
		if (!testCon(druidDataSource)) {
			log.error("数据库连接失败!");
		}
		// 连接池中保留的最大连接数
		druidDataSource.setMaxActive(10);
		druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
		druidDataSource.setMaxWait(10000);
		// 每次连接不校验有效性，提升性能
		druidDataSource.setTestOnBorrow(false);
		druidDataSource.setTestOnReturn(false);
		log.info("数据库连接初始化成功！");


	}

	public Connection getConnection(){
		try {
			return druidDataSource.getConnection();
		} catch (Exception e) {
		    log.error("数据库连接失败！");
			throw new RuntimeException("数据库连接失败！");
		}

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
}
