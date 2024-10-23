package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (AsynTaskRecord)实体类
 *
 * @author beixing
 * @since 2021-11-30 18:06:52
 */
@Data
@Table(name = "asyn_task_record")
public class AsynTaskRecord implements Serializable {
    private static final long serialVersionUID = 761344107304994745L;
    @Column(name = "id")
    private Long id;
    /**
     * 任务ID，唯一
     */
    @Column(name = "task_id")
    private String taskId;
    /**
     * 来源项目编号
     */
    @Column(name = "source_project_id")
    private String sourceProjectId;
    /**
     * 目标项目编号
     */
    @Column(name = "target_project_id")
    private String targetProjectId;
    /**
     * 队列名称
     */
    @Column(name = "queue_name")
    private String queueName;
    /**
     * 请求数据
     */
    @Column(name = "req_data")
    private String reqData;
    /**
     * 响应数据
     */
    @Column(name = "resp_data")
    private String respData;
    /**
     * 执行状态 0,未开始,1,进行中
     */
    @Column(name = "execute_status")
    private Integer executeStatus;
    /**
     * 重试次数
     */
    @Column(name = "retry_count")
    private Integer retryCount;
    /**
     * 失败原因
     */
    @Column(name = "fail_reason")
    private String failReason;
    /**
     * 开始时间
     */
    @Column(name = "create_time")
    private String createTime;
    /**
     * 消费时间
     */
    @Column(name = "consumer_time")
    private String consumerTime;
    /**
     * 失败时间
     */
    @Column(name = "fail_time")
    private String failTime;
    /**
     * 接收时间
     */
    @Column(name = "receive_time")
    private String receiveTime;
    /**
     * 完成时间
     */
    @Column(name = "finish_time")
    private String finishTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
