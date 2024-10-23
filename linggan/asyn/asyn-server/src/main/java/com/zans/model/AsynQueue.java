package com.zans.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (AsynQueue)实体类
 *
 * @author beixing
 * @since 2021-11-30 17:36:08
 */
@Data
@Table(name = "asyn_queue")
public class AsynQueue implements Serializable {
    private static final long serialVersionUID = 192812051383112777L;
    @Column(name = "id")
    private Integer id;
    /**
     * 队列名称
     */
    @Column(name = "queue_name")
    private String queueName;
    /**
     * 队列描述
     */
    @Column(name = "queue_desc")
    private String queueDesc;
    /**
     * 队列启用状态
     */
    @Column(name = "queue_enable")
    private Integer queueEnable;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
