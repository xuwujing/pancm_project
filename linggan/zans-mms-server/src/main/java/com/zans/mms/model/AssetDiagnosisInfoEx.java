package com.zans.mms.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: (AssetDiagnosisInfoEx)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-14 14:42:27
 */
@Data
@Table(name = "asset_diagnosis_info_ex")
public class AssetDiagnosisInfoEx implements Serializable {
    private static final long serialVersionUID = 181504003735380289L;
    /**
     * 自增id
     */
    @Column(name = "id")
    private Long id;
    /**
     * 设备编号
     */
    @Column(name = "asset_code")
    private String assetCode;
    /**
     * 追踪标记
     */
    @Column(name = "trace_id")
    private String traceId;
    /**
     * 诊断结果 1 正常 2 异常
     */
    @Column(name = "diagnosis_result")
    private Integer diagnosisResult;
    /**
     * 图片故障类型
     */
    @Column(name = "fault_type")
    private Integer faultType;
    /**
     * 诊断类型结果 0 未检测 1 正常 2 异常
     */
    @Column(name = "fault_type_result")
    private Integer faultTypeResult;
    /**
     * 图片路径
     */
    @Column(name = "img_url")
    private String imgUrl;
    /**
     * 诊断时间
     */
    @Column(name = "diagnosis_time")
    private String diagnosisTime;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
