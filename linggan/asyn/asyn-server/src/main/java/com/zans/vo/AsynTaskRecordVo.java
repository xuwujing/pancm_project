package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * (AsynTaskRecord)实体类
 *
 * @author beixing
 * @since 2021-11-22 14:37:44
 */
@Data
public class AsynTaskRecordVo implements Serializable {
    private static final long serialVersionUID = -35102323658076956L;
    private Long id;
    /**
     * 任务ID，唯一
     */
    private String taskId;
    /**
     * 来源项目编号
     */
    private String sourceProjectId;
    /**
     * 目标项目编号
     */
    private String targetProjectId;
    /**
     * 请求数据
     */
    private String reqData;
    /**
     * 响应数据
     */
    private String respData;
    /**
     * 执行状态 0,未开始
     */
    private Integer executeStatus;
    /**
     * 重试次数
     */
    private Integer retryCount;
    private String createTime;
    private String updateTime;

    /**
     * 地址
     */
    private String targetUrl;
    /**
     * 协议 HTTP或HTTPS
     */
    private String urlHttp;
    /**
     * 请求方法，GET或POST
     */
    private String urlMethod;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
