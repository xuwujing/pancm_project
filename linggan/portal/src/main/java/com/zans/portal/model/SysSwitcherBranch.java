package com.zans.portal.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 汇聚点功能
 */
@Table(name = "sys_switcher_branch")
@Data
public class SysSwitcherBranch implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer projectId;// '项目名称',
//    private String projectName;// '项目名称',
    @Deprecated  //使用sys_switcher表 sysName
    private String sysName;//  '交换机系统名',
    @Deprecated//使用asset表
    private String pointName;//`  '点位名称',
    private String ipAddr;//` varchar(255) NOT NULL COMMENT 'IP',
    private BigDecimal lon;//` float(10, 6) NULL COMMENT '经度',
    private BigDecimal lat;//` float(10, 6) NULL COMMENT '维度',
    private Integer  region;// int(10) NULL COMMENT '片区',
    private String model;//  '硬件型号',
    private Integer brand;// int(10) NULL COMMENT '品牌',
    @Deprecated //使用asset表
    private Integer status=0;//  '状态'0启用 1 停用,
    private Integer acceptance=0;//验收状态 1验收
    private Date acceptDate;//验收时间
    private String acceptIdea;//验收意见
    private String reason;//处置原因
    @Deprecated //使用asset表
    private Integer online;//在线状态 1在线 0离线
    private Integer swType;//   '交换机类型，0core 核心交换机; 1convergence 汇聚交换机; 2access',接入交换机
    private String swAccount;//`  '交换机账户',
    private String swPassword;//`  '交换机密码',
    private String community;// 'snmp名称',

    private Integer deleteStatus=0;//0,正常；1，删除
    private Date createTime;
    private Date updateTime;
    private Integer consBatch;//建设批次1 一期 2二期
    
}
