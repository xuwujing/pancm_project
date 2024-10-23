package com.zans.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "alive_heartbeat")
public class AliveHeartbeat implements Serializable {
    @Id
    private Long id;

    private String username;

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
