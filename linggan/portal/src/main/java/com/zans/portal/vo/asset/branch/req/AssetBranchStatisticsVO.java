package com.zans.portal.vo.asset.branch.req;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel(description="资产子集统计数据")
@Data
public class AssetBranchStatisticsVO {
    /**
     * SELECT abe.id assetBranchEndpointId, abe.username,abe.pass,e.enable_status,p.alive,p.alive_last_time aliveLastTime
     *  FROM asset_branch_endpoint abe
     *  inner join radius_endpoint e  on abe.username=e.username
     *  inner join radius_endpoint_profile p on abe.username = p.username
     *  WHERE abe.asset_branch_id=1 and e.delete_status=0
     */
//    @ApiModelProperty(name = "assetBranchEndpointId", value = "设备分组关联表id")
//    private Integer assetBranchEndpointId;
    @ApiModelProperty(name = "assetBranchAssetId", value = "设备分组关联表id")
    private Integer assetBranchAssetId;

    @ApiModelProperty(name = "assetBranchName", value = "设备分组名")
    private String  assetBranchName;

    @ApiModelProperty(name = "assetId", value = "资产id")
    private Integer assetId;

    @ApiModelProperty(name = "mac", value = "mac")
    private String mac;


    @ApiModelProperty(name = "alive", value = "1.在线；2.离线")
    private Integer alive;

    @ApiModelProperty(name = "aliveName", value = "1.在线；2.离线")
    private String aliveName;

    @ApiModelProperty(name = "aliveLastTime", value = "最后一次离线时间")
    @JSONField(name = "aliveLastTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date aliveLastTime;

    @ApiModelProperty(name = "pointName", value = "点位")
    private String pointName;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceTypeName", value = "deviceTypeName")
    private String deviceTypeName;

    @ApiModelProperty(name = "brandName", value = "品牌")
    private String brandName;

    @ApiModelProperty(name = "company", value = "厂商")
    private String  company;

    @ApiModelProperty(name = "modelDes", value = "设备型号")
    private String modelDes;

    @ApiModelProperty(name = "ipAddr", value = "ip地址")
    private String ipAddr;

    @ApiModelProperty(name = "enableStatus", value = "0，停用;1,启用")
    private Integer enableStatus;

    @ApiModelProperty(name = "enableStatus", value = "0，停用;1,启用")
    private String enableStatusName;

    @ApiModelProperty(name = "longitude", value = "经度")
    private String longitude;

    @ApiModelProperty(name = "latitude", value = "纬度")
    private String latitude;


    public void setAliveByMap(Map<Object, String> map) {
        Integer status = this.getAlive();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setAliveName(name);
    }
    /**
     * 设置资产状态所对应的名称
     * @param map
     */
    public void setEnableStatusNameByMap(Map<Object, String> map){
        Integer status = this.enableStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setEnableStatusName(name);
    }
}
