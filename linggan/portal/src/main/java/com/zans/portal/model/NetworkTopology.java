package com.zans.portal.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "network_topology")
public class NetworkTopology implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 源IP
     */
    @Column(name = "source_ip")
    private String sourceIp;

    /**
     * 源端口
     */
    @Column(name = "source_interface")
    private String sourceInterface;

    /**
     * 目的IP
     */
    @Column(name = "dest_ip")
    private String destIp;

    /**
     * 目的端口
     */
    @Column(name = "dest_interface")
    private String destInterface;

    /**
     * 接口类型：1up上行口，2down下行口;
     */
    private String remark;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "visible")
    private Integer visible;

    private static final long serialVersionUID = 1L;


}