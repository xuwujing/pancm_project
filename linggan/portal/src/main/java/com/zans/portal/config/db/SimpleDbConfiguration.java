package com.zans.portal.config.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class SimpleDbConfiguration {

    @Bean(name = "radiusDataSource")
    @Qualifier("radiusDataSource")
    @ConfigurationProperties(prefix="spring.datasource.radius")
    public DataSource radiusDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "radiusJdbcTemplate")
    public JdbcTemplate radiusJdbcTemplate(
            @Qualifier("radiusDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
