package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author pancm
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/12/23
 */
@Table(name = "radius_endpoint_log")
@Data
public class RadiusEndpointLog implements Serializable {

    /**
     * 终端mac地址，17位
     */
    private String username;

    /**
     * 0, 阻断；1，检疫区，2，放行
     */
    @Column(name = "access_policy")
    private Integer accessPolicy;

    /**
     * 0, 正常； 1，删除
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;

    @Column(name = "data_source")
    private Integer dataSource;

    @Column(name = "create_time")
    private String createTime;


}
