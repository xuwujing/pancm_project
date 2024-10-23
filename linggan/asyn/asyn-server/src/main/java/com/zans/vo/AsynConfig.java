package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * (AsynConfig)实体类
 *
 * @author beixing
 * @since 2021-11-22 14:23:50
 */
@Data
public class AsynConfig implements Serializable {
    private static final long serialVersionUID = -67782154222487560L;
    private Integer id;
    /**
     * 项目编号
     */
    private String projectId;
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
    /**
     * 队列名称
     */
    private String queueName;
    /**
     * 启用状态 0,否;1,是
     */
    private Integer enable;
    private String createTime;
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
