package com.zans.portal.vo.ip.req;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.vo.ip.IpEditVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value="ipAll编辑对象",description="ipAll编辑对象")
public class IpAllEditReqVO extends IpEditVO{

    @ApiModelProperty(value = "所属部门单位")
    @JSONField(name = "department_id")
    private Integer departmentId;

    @ApiModelProperty(value = "接入点的经度")
    private String longitude;

    @ApiModelProperty(value = "接入点的纬度")
    private String latitude;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
