package com.zans.mms.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author beixing
 * @Title: (AssetDiagnosisInfo)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-05-14 14:55:52
 */
@Data
@Table(name = "asset_diagnosis_info")
public class AssetDiagnosisInfo implements Serializable {
    private static final long serialVersionUID = -68948717389885887L;
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
     * 图片故障类型集合，用逗号隔开
     */
    @Column(name = "fault_types")
    private String faultTypes;
    /**
     * 图片故障类型名称集合，用逗号隔开
     */
    @Column(name = "fault_types_name")
    private String faultTypesName;
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
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "ipAddress")
    private String ipAddress;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
