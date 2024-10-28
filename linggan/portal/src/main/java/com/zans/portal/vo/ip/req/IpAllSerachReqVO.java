package com.zans.portal.vo.ip.req;

import com.alibaba.fastjson.JSONObject;
import com.zans.portal.vo.ip.IpSearchVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class IpAllSerachReqVO extends IpSearchVO{

    @ApiModelProperty(value = "设备编号")
    private String deviceModelDes;


    @ApiModelProperty(value = "所属部门单位")
    private Integer departmentId;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
