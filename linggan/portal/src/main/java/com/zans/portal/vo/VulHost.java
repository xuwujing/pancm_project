package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (VulHost)实体类
 *
 * @author beixing
 * @since 2021-11-25 16:58:24
 */
@Data
public class VulHost implements Serializable {
    private static final long serialVersionUID = -94961468328929711L;
    private Long id;
    private String jobId;
    /**
     * 任务状态
     */
    private Integer jobStatus;
    /**
     * ipv4格式
     */
    private String ipAddr;
    /**
     * 漏洞对应的开放端口列表
     * 无，用0标识；
     * 多个开放端口，用英文逗号分割，如80,81,8080
     */
    private String port;
    /**
     * 极危个数，红色
     */
    private Integer critical;
    /**
     * 高危个数，橙色
     */
    private Integer high;
    /**
     * 中危个数，黄色
     */
    private Integer medium;
    /**
     * 低危个数，绿色
     */
    private Integer low;
    /**
     * 提示个数，蓝色
     */
    private Integer info;
    /**
     * 风险评分
     */
    private String score;
    /**
     * 风险总数
     */
    private Integer total;
    /**
     * 风险等级
     */
    private String level;
    private Date createTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
