package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: 基线表(AssetBaseline)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:09:39
 */
@ApiModel(value = "AssetBaseline", description = "基线表")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetBaselineBVO  implements Serializable {
    private static final long serialVersionUID = 855698942672698570L;
    @ApiModelProperty(value = "${column.comment}")
    private Integer id;
    /**
     * endpoint_profile放行时候的cur_ip_addr
     */
    @ApiModelProperty(value = "endpoint_profile放行时候的cur_ip_addr")
    private String ipAddr;
    /**
     * 终端mac地址
     */
    @ApiModelProperty(value = "终端mac地址")
    private String username;
    /**
     * endpoint_profile放行时候的cur_model_des
     */
    @ApiModelProperty(value = "endpoint_profile放行时候的cur_model_des")
    private String modelDes;
    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brandName;
    /**
     * endpoint_profile放行时候的cur_device_type
     */
    @ApiModelProperty(value = "endpoint_profile放行时候的cur_device_type")
    private Integer deviceType;

    private String deviceTypeName;
    /**
     * endpoint_profile放行时候的cur_server_os
     */
    @ApiModelProperty(value = "endpoint_profile放行时候的cur_server_os")
    private String serverOs;

    /**
     * 虚拟局域网网络
     */
    @ApiModelProperty(value = "虚拟局域网网络")
    private String vlan;
    /**
     * 点位名称
     */
    @ApiModelProperty(value = "点位名称")
    private String pointName;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private String latitude;


    private Integer parentId;


    private Integer seq;

    private Integer assetBranchId;


    private String name;

    private Integer level;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }


}
