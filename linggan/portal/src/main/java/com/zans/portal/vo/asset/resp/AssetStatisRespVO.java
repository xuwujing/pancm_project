package com.zans.portal.vo.asset.resp;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资产统计报表数据
 */
@Data
public class AssetStatisRespVO {

    @ApiModelProperty(name = "id", value = "标识符")
    private Integer id;

    @ApiModelProperty(name = "name", value = "统计维度")
    private String name;

    @ApiModelProperty(name = "deviceTypeName", value = "统计维度")
    private String deviceTypeName;

    @ApiModelProperty(name = "totalNum", value = "总设备数量")
    private Long totalNum = 0L;

    @ApiModelProperty(name = "offlineNum", value = "离线设备数量")
    private Long offlineNum = 0L;

    @ApiModelProperty(name = "disableNum", value = "停用设备数量")
    private Long disableNum = 0L;

    @ApiModelProperty(name = "aliveNum", value = "在线数量")
    private Long aliveNum = 0L;

    @ApiModelProperty(name = "onlineRate", value = "在线率")
    private Double onlineRate = 0D;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
