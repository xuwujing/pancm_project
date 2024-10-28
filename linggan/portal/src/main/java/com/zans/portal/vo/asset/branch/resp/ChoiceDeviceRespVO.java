package com.zans.portal.vo.asset.branch.resp;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel("资产子集选择设备返回体")
@Data
public class ChoiceDeviceRespVO {

    @ApiModelProperty(name = "assetId", value = "资产id")
    private Integer assetId;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceTypeName", value = "设备类型名称")
    private String deviceTypeName;

    @ApiModelProperty(name = "ipAddr", value = "ip地址")
    private String ipAddr;

    @ApiModelProperty(name = "mac", value = "mac地址")
    private String mac;



    @ApiModelProperty(name = "pointName", value = "点位名称")
    private String pointName;

    @ApiModelProperty(name = "company", value = "厂商")
    private String company;

    @ApiModelProperty(name = "brandName", value = "品牌")
    private String brandName;

    @ApiModelProperty(name = "curModelDes", value = "设备型号")
    private String curModelDes;

    @ApiModelProperty(name = "alive", value = "在线状态，1.在线；2.离线")
    private Integer alive;

    @ApiModelProperty(name = "aliveName", value = "在线状态，1.在线；2.离线")
    private String aliveName;

    @ApiModelProperty(name = "createTime", value = "入网时间")
    @JSONField(name = "createTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date  createTime;


    public void setAliveByMap(Map<Object, String> map) {
        Integer status = this.getAlive();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setAliveName(name);
    }

    public boolean equals(Object obj) {
        ChoiceDeviceRespVO u = (ChoiceDeviceRespVO) obj;
        return assetId.equals(u.assetId);
    }

    public int hashCode() {
        Integer in = assetId;
        return in.hashCode();
    }
}
