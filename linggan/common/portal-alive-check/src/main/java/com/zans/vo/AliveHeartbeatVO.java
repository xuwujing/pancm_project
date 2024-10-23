package com.zans.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author qiyi
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/5
 */
@Data
public class AliveHeartbeatVO {

    @Id
    private Long id;

    private String mac;

    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * 是否在线;1:在线；2；不在线
     */
    private Integer alive;

    /**
     * 上次在线时间
     */
    @Column(name = "alive_last_time")
    private Date aliveLastTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}
