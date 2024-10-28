package com.zans.portal.vo.ip.req;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value="ipAll新增对象",description="ipAll新增对象")
public class IpAllAddReqVO {

    @ApiModelProperty(value = "接入点的IP",required = true)
    @NotNull(message = "ip不能为空")
    private String ipAddr;

    @ApiModelProperty(value = "设备编号",required = true)
    @NotNull(message = "设备不能为空")
    private String deviceModelDes;

    @ApiModelProperty(value = "点位名称",required = true)
    @NotNull(message = "点位不能为空")
    private String pointName;

    @ApiModelProperty(value = "所属部门单位",required = true)
    @NotNull(message = "单位不能为空")
    private Integer departmentId;



    @ApiModelProperty(value = "接入点的经度",required = true)
    @NotNull(message = "经度不能为空")
    private String longitude;

    @ApiModelProperty(value = "接入点的纬度",required = true)
    @NotNull(message = "纬度不能为空")
    private String latitude;

    @ApiModelProperty(value = "项目名称",required = true)
    @NotNull(message = "项目名称不能为空")
    private String projectName;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

