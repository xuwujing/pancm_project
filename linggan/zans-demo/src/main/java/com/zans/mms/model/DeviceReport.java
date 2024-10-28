package com.zans.mms.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 设备上报(DeviceReport)实体类
 *
 * @author beixing
 * @since 2021-03-18 12:18:52
 */
@ApiModel(value = "DeviceReport", description = "设备上报")
@Data
@Table(name = "device_report")
public class DeviceReport implements Serializable {
    private static final long serialVersionUID = 173544497218914956L;
    @Column(name = "id")
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    /**
     * 辖区ID
     */
    @Column(name = "area_id")
    @ApiModelProperty(value = "辖区ID")
    private String areaId;
    /**
     * 上报的地址
     */
    @Column(name = "address")
    @ApiModelProperty(value = "上报的地址")
    private String address;
    /**
     * 设备类型
     */
    @Column(name = "device_type")
    @ApiModelProperty(value = "设备类型")
    private String deviceType;
    /**
     * 上报状态
     */
    @Column(name = "report_status")
    @ApiModelProperty(value = "上报状态")
    private Integer reportStatus;
    /**
     * 附件ID
     */
    @Column(name = "adjunct_id")
    @ApiModelProperty(value = "附件ID")
    private String adjunctId;
    /**
     * 建设年份
     */
    @Column(name = "build_year")
    @ApiModelProperty(value = "建设年份")
    private String buildYear;
    /**
     * 建设单位
     */
    @Column(name = "build_company")
    @ApiModelProperty(value = "建设单位")
    private String buildCompany;
    /**
     * 建设单位联系人
     */
    @Column(name = "build_contact")
    @ApiModelProperty(value = "建设单位联系人")
    private String buildContact;
    /**
     * 建设单位联系电话
     */
    @Column(name = "build_phone")
    @ApiModelProperty(value = "建设单位联系电话")
    private String buildPhone;
    /**
     * 运维单位名称
     */
    @Column(name = "maintain_company")
    @ApiModelProperty(value = "运维单位名称")
    private String maintainCompany;
    /**
     * 运维单位联系人
     */
    @Column(name = "maintain_contact")
    @ApiModelProperty(value = "运维单位联系人")
    private String maintainContact;
    /**
     * 运维单位联系电话
     */
    @Column(name = "maintain_phone")
    @ApiModelProperty(value = "运维单位联系电话")
    private String maintainPhone;
    /**
     * 设备状态
     */
    @Column(name = "device_status")
    @ApiModelProperty(value = "设备状态")
    private Integer deviceStatus;
    /**
     * 是否启用违法
     */
    @Column(name = "enable_illegal")
    @ApiModelProperty(value = "是否启用违法")
    private Integer enableIllegal;
    /**
     * 描述
     */
    @Column(name = "remark")
    @ApiModelProperty(value = "描述")
    private String remark;
    /**
     * gis
     */
    @Column(name = "gis")
    @ApiModelProperty(value = "gis")
    private Object gis;
    /**
     * 创建用户
     */
    @Column(name = "creator")
    @ApiModelProperty(value = "创建用户")
    private String creator;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
