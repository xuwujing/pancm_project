package com.zans.job.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.zans.job.service.IExecutorService;
import com.zans.job.vo.common.NodeDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @author xv
 * @since 2020/7/22 7:19
 */
@Slf4j
@Service
public class ExecutorServiceImpl implements IExecutorService {

    @Autowired
    private DataSource jobDataSource;

    @Autowired
    @Qualifier("businessDataSource")
    private DataSource businessDataSource;

    @Override
    public NodeDataSource getJobDataSource() {
        if (jobDataSource instanceof DruidDataSource) {
            return this.parseJdbcUrl((DruidDataSource)jobDataSource);
        } else {
            log.error("ds class error#{}", jobDataSource.getClass());
            return null;
        }
    }

    /**
     * jdbc:mysql://192.168.231.1:3306/guard_job?
     * @param druidDataSource
     * @return
     */
    private NodeDataSource parseJdbcUrl(DruidDataSource druidDataSource) {
        DruidDataSource ds = (DruidDataSource)druidDataSource;
        String url = ds.getUrl();
        String username = ds.getUsername();
        String password = ds.getPassword();
        log.info("DruidDataSource class url#{}, {}", url, username);
        int endIndex = url.indexOf("?");
        String cleanUrl = url;
        if (endIndex != -1) {
            cleanUrl = url.substring(0, endIndex);
        }
        String[] splits = cleanUrl.split("/");
        if (splits.length != 4) {
            return null;
        }
        String urlPart = splits[2];
        String db = splits[3];
        String host = "";
        int port = 3306;
        if (urlPart.contains(":")) {
            String[] urlSplit = urlPart.split(":");
            port = Integer.parseInt(urlSplit[1]);
            host = urlSplit[0];
        } else {
            host = urlPart;
        }
        NodeDataSource nds = NodeDataSource.builder().db(db).host(host).port(port).user(username).passwd(password).build();
        return nds;
    }

    @Override
    public NodeDataSource getBusinessDataSource() {
        if (businessDataSource instanceof DruidDataSource) {
            return this.parseJdbcUrl((DruidDataSource)businessDataSource);
        } else {
            log.error("ds class error#{}", businessDataSource.getClass());
            return null;
        }
    }
}
