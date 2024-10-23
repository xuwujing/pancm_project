package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (AsynConfig)实体类
 *
 * @author beixing
 * @since 2021-11-22 14:23:51
 */
@Data
@Table(name = "asyn_config")
public class AsynConfig implements Serializable {
    private static final long serialVersionUID = -39782734759843174L;
    @Column(name = "id")
    private Integer id;
    /**
     * 项目编号
     */
    @Column(name = "project_id")
    private String projectId;
    /**
     * 地址
     */
    @Column(name = "target_url")
    private String targetUrl;
    /**
     * 协议 HTTP或HTTPS
     */
    @Column(name = "url_http")
    private String urlHttp;
    /**
     * 请求方法，GET或POST
     */
    @Column(name = "url_method")
    private String urlMethod;
    /**
     * 队列名称
     */
    @Column(name = "queue_name")
    private String queueName;
    /**
     * 启用状态 0,否;1,是
     */
    @Column(name = "enable")
    private Integer enable;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
