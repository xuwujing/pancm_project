package com.zans.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

import static com.zans.config.SimpleDbConfiguration.PACKAGE;

@Configuration
@EnableConfigurationProperties({MultipleJdbcProperties.class})
@MapperScan(basePackages = {PACKAGE}, sqlSessionFactoryRef = "radiusSqlSessionFactory")
public class SimpleDbConfiguration {
    static final String PACKAGE = "com.zans.radius";
    static final String MAPPER_LOCATION = "classpath:radius/mapper/*.xml";
    static final String CONFIG_LOCATION = "classpath:mybatis-config.xml";

    @Autowired
    private MultipleJdbcProperties multipleJdbcProperties;


    @Bean(name = "radiusDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.radius")
    public DataSource radiusDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(multipleJdbcProperties.getRadius().getDriverClassName());
        dataSource.setUrl(multipleJdbcProperties.getRadius().getUrl());
        dataSource.setUsername(multipleJdbcProperties.getRadius().getUsername());
        dataSource.setPassword(multipleJdbcProperties.getRadius().getPassword());
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(30);
        dataSource.setMaxWait(30000);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(1800);
        dataSource.setLogAbandoned(false);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(500);
        dataSource.setKeepAlive(true);
        dataSource.setDbType("mysql");
        return dataSource;
    }

    @Bean(name = "radiusSqlSessionFactory")
    public SqlSessionFactory radiusSqlSessionFactory(@Qualifier("radiusDataSource") DataSource dataSource) throws Exception {
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

    @Bean(name = "radiusTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("radiusDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "radiusSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("radiusSqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }



    @Bean(name = "radiusJdbcTemplate")
    public JdbcTemplate radiusJdbcTemplate(
            @Qualifier("radiusDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
