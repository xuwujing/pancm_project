package com.zans.job.vo.common;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * node 的数据源配置
 * @author xv
 * @since 2020/7/22 7:14
 */
@Builder
@Data
public class NodeDataSource {

    private String host;

    private Integer port;

    private String user;

    private String passwd;

    private String db;
}
