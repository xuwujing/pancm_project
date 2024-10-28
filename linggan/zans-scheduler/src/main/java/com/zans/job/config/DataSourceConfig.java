package com.zans.job.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

/**
 * @author xv
 * @since 2020/5/6 16:46
 */
@Configuration
@MapperScan(basePackages = {DataSourceConfig.PACKAGE}, sqlSessionFactoryRef = "jobSqlSessionFactory")
public class DataSourceConfig {

    static final String PACKAGE = "com.zans.job.dao";
    static final String MAPPER_LOCATION = "classpath:job/mapper/*.xml";
    static final String CONFIG_LOCATION = "classpath:job/mybatis-config.xml";

    @Bean(name = "businessDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.business")
    public DataSource businessDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "jobDataSource")
    @Primary
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource.druid.job")
    public DataSource jobDataSource(){
        return new DruidDataSource();
    }

    @Bean(name = "jobSqlSessionFactory")
    @Primary
    public SqlSessionFactory jobSqlSessionFactory(@Qualifier("jobDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);

        //添加插件
        bean.setPlugins(new Interceptor[]{pageHelper});

        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(CONFIG_LOCATION));
        Objects.requireNonNull(bean.getObject()).getConfiguration().setMapUnderscoreToCamelCase(true);
        return bean.getObject();
    }

    @Bean(name = "jobTransactionManager")
    @Primary
    public DataSourceTransactionManager jobTransactionManager(@Qualifier("jobDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "jobSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate jobSqlSessionTemplate(@Qualifier("jobSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
