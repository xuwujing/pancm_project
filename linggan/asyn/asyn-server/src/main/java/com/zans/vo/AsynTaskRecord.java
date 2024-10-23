package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * (AsynTaskRecord)实体类
 *
 * @author beixing
 * @since 2021-11-24 19:10:54
 */
@Data
public class AsynTaskRecord implements Serializable {
    private static final long serialVersionUID = -59651676533411828L;
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
     * 队列名称
     */
    private String queueName;
    /**
     * 请求数据
     */
    private String reqData;
    /**
     * 响应数据
     */
    private String respData;
    /**
     * 执行状态 0,未开始,1,进行中
     */
    private Integer executeStatus;
    /**
     * 重试次数
     */
    private Integer retryCount;
    /**
     * 开始时间
     */
    private String createTime;
    /**
     * 消费时间
     */
    private String consumerTime;
    /**
     * 接收时间
     */
    private String receiveTime;
    /**
     * 完成时间
     */
    private String finishTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
