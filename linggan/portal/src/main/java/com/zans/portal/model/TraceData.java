package com.zans.portal.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author beixing
 * @Title: (TraceData)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022-01-18 12:15:55
 */
@Data
@Table(name = "trace_data")
public class TraceData implements Serializable {
    private static final long serialVersionUID = 246748127116245465L;
    @Column(name = "id")
    private Long id;
    /**
     * 唯一值
     */
    @Column(name = "trace_id")
    private String traceId;
    /**
     * 类型,1:准入,2:风险
     */
    @Column(name = "trace_type")
    private Integer traceType;
    /**
     * 来源,portal,judge_api
     */
    @Column(name = "trace_source")
    private String traceSource;
    /**
     * 目标:portal,judge_api,rad_api
     */
    @Column(name = "trace_target")
    private String traceTarget;
    /**
     * 是否失败
     */
    @Column(name = "is_fail")
    private Integer isFail;
    /**
     * 顺序
     */
    @Column(name = "trace_order")
    private Integer traceOrder;
    /**
     * 请求响应参数
     */
    @Column(name = "req_data")
    private String reqData;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 日志时间
     */
    @Column(name = "log_time")
    private Object logTime;
    @Column(name = "create_time")
    private String createTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
