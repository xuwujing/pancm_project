package com.zans.mms.vo.devicereport;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 设备上报(DeviceReport)实体类
 *
 * @author beixing
 * @since 2021-03-08 17:21:38
 */
@ApiModel(value = "DeviceReportSaveReq", description = "设备上报新增")
@Data
public class DeviceReportSaveReqVO implements Serializable {
    private static final long serialVersionUID = 559836544856738398L;


    private Long id;
    /**
     * 辖区ID
     */
    @ApiModelProperty(value = "辖区ID")
    private String areaId;
    /**
     * 上报的地址
     */
    @ApiModelProperty(value = "上报的地址")
    private String address;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    /**
     * 附件ID
     */
    @ApiModelProperty(value = "附件ID")
    private String adjunctId;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;
    /**
     * 创建用户
     */
    @ApiModelProperty(value = "创建用户")
    private String creator;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String gis;

    /**
     * 建设年份
     */
    @ApiModelProperty(value = "建设年份")
    private String buildYear;
    /**
     * 建设单位
     */
    @ApiModelProperty(value = "建设单位")
    private String buildCompany;
    /**
     * 建设单位联系人
     */
    @ApiModelProperty(value = "建设单位联系人")
    private String buildContact;
    /**
     * 建设单位联系电话
     */
    @ApiModelProperty(value = "建设单位联系电话")
    private String buildPhone;
    /**
     * 运维单位名称
     */
    @ApiModelProperty(value = "运维单位名称")
    private String maintainCompany;
    /**
     * 运维单位联系人
     */
    @ApiModelProperty(value = "运维单位联系人")
    private String maintainContact;
    /**
     * 运维单位联系电话
     */
    @ApiModelProperty(value = "运维单位联系电话")
    private String maintainPhone;
    /**
     * 设备状态
     */
    @ApiModelProperty(value = "设备状态")
    private Integer deviceStatus;
    /**
     * 是否启用违法
     */
    @ApiModelProperty(value = "是否启用违法")
    private Integer enableIllegal;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
