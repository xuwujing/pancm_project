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
 * @since 2021-11-22 14:37:45
 */
@Data
@Table(name = "asyn_task_record")
public class AsynTaskRecord implements Serializable {
    private static final long serialVersionUID = 909727943203717680L;
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

    @Column(name = "queue_name")
    private String queueName;
    /**
     * 目标项目编号
     */
    @Column(name = "target_project_id")
    private String targetProjectId;
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
     * 执行状态 0,未开始
     */
    @Column(name = "execute_status")
    private Integer executeStatus;
    /**
     * 重试次数
     */
    @Column(name = "retry_count")
    private Integer retryCount;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
