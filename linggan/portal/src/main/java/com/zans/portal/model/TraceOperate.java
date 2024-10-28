package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (TraceOperate)实体类
 *
 * @author beixing
 * @since 2022-02-28 17:47:55
 */
@Data
@Table(name = "trace_operate")
public class TraceOperate extends BasePage implements Serializable {
    private static final long serialVersionUID = -63819530613260906L;
    @Column(name = "id")
    private Long id;
    @Column(name = "trace_id")
    private String traceId;
    /**
     * 类型,1:准入,2:风险
     */
    @Column(name = "trace_type")
    private Integer traceType;
    /**
     * 起止类型
     */
    @Column(name = "trace_start_type")
    private String traceStartType;
    /**
     * 结束类型
     */
    @Column(name = "trace_end_type")
    private String traceEndType;
    /**
     * 成功或失败
     */
    @Column(name = "trace_result")
    private String traceResult;
    /**
     * 创建者
     */
    @Column(name = "creator")
    private String creator;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
