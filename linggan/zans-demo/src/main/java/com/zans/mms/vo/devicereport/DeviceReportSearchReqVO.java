package com.zans.mms.vo.devicereport;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 设备上报(DeviceReport)实体类
 *
 * @author beixing
 * @since 2021-03-08 17:21:38
 */
@ApiModel(value = "DeviceReportSearchReqVO", description = "设备上报查询")
@Data
public class DeviceReportSearchReqVO extends BasePage implements Serializable {
    private static final long serialVersionUID = 559836544856738398L;


    /**
     * 辖区ID
     */
    @ApiModelProperty(value = "辖区ID")
    private List<String> areaId;
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

    @ApiModelProperty(value = "上报状态")
    private Integer reportStatus;

    /**
     * 创建用户
     */
    @ApiModelProperty(value = "创建用户")
    private String creator;

    private String searchLike;

    @ApiModelProperty(value = "数据权限  1 全部  2单位  3个人 ",name = " 数据权限  1 全部  2单位  3个人 ")
    private Integer dataPerm;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
