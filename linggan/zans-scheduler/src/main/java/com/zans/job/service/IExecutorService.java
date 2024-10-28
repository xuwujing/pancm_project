package com.zans.job.service;

import com.zans.job.vo.common.NodeDataSource;

import javax.sql.DataSource;

/**
 * @author xv
 * @since 2020/7/22 7:17
 */
public interface IExecutorService {

    /**
     * 获得 guard_job 的连接配置信息
     * @return
     */
    NodeDataSource getJobDataSource();

    /**
     * 获得 guard_scan 的连接配置信息
     * @return
     */
    NodeDataSource getBusinessDataSource();
}
