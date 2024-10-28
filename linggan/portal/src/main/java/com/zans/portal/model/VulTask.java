package com.zans.portal.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "vul_task")
public class VulTask implements Serializable {
    @Id
    private Long id;

    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * 第三方漏洞id
     */
    @Column(name = "job_id")
    private String jobId;

    private String name;

    private String status;

    /**
     * 进度
     */
    private String progress;

    /**
     * 获取扫描结果次数
     */
    @Column(name = "ret_get_count")
    private Integer retGetCount;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}