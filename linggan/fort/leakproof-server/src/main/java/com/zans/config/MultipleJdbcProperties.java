package com.zans.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "datasource")
@Component
public class MultipleJdbcProperties {

    /**
     * guard连接配置对象
     */
    DataSourceProperties guard = new DataSourceProperties();

    public DataSourceProperties getGuard() {
        return guard;
    }

    public void setGuard(DataSourceProperties guard) {
        this.guard = guard;
    }


}
