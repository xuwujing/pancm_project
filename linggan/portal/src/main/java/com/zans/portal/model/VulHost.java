package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import com.zans.portal.vo.chart.CircleUnit;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (VulHost)实体类
 *
 * @author beixing
 * @since 2021-11-25 16:58:25
 */
@Data
@Table(name = "vul_host")
public class VulHost implements Serializable {
    private static final long serialVersionUID = -80013818855092402L;
    @Column(name = "id")
    private Long id;
    @Column(name = "job_id")
    private String jobId;
    /**
     * 任务状态
     */
    @Column(name = "job_status")
    private Integer jobStatus;
    /**
     * ipv4格式
     */
    @Column(name = "ip_addr")
    private String ipAddr;
    /**
     * 漏洞对应的开放端口列表
     * 无，用0标识；
     * 多个开放端口，用英文逗号分割，如80,81,8080
     */
    @Column(name = "port")
    private String port;
    /**
     * 极危个数，红色
     */
    @Column(name = "critical")
    private Integer critical;
    /**
     * 高危个数，橙色
     */
    @Column(name = "high")
    private Integer high;
    /**
     * 中危个数，黄色
     */
    @Column(name = "medium")
    private Integer medium;
    /**
     * 低危个数，绿色
     */
    @Column(name = "low")
    private Integer low;
    /**
     * 提示个数，蓝色
     */
    @Column(name = "info")
    private Integer info;
    /**
     * 风险评分
     */
    @Column(name = "score")
    private String score;
    /**
     * 风险总数
     */
    @Column(name = "total")
    private Integer total;
    /**
     * 风险等级
     */
    @Column(name = "level")
    private String level;
    @Column(name = "create_time")
    private Date createTime;

    private List<CircleUnit> circleUnits;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
