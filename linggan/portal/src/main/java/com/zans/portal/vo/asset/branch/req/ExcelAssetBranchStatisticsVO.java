package com.zans.portal.vo.asset.branch.req;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@ApiModel(description="资产子集统计数据excel")
@Data
public class ExcelAssetBranchStatisticsVO {
    /**
     * SELECT abe.id assetBranchEndpointId, abe.username,abe.pass,e.enable_status,p.alive,p.alive_last_time aliveLastTime
     *  FROM asset_branch_endpoint abe
     *  inner join radius_endpoint e  on abe.username=e.username
     *  inner join radius_endpoint_profile p on abe.username = p.username
     *  WHERE abe.asset_branch_id=1 and e.delete_status=0
     */
    @ApiModelProperty(name = "assetBranchEndpointId", value = "设备分组关联表id")
    @ExcelProperty(value = "设备分组关联表id", ignore = true)
    private Integer assetBranchEndpointId;

    @ApiModelProperty(name = "assetBranchName", value = "设备分组名")
    @ExcelProperty(value = "分组名称", index = 1, width = 15)
    private String  assetBranchName;


    @ApiModelProperty(name = "areaName", value = "所属辖区")
    @ExcelProperty(value = "所属辖区", index = 2)
    private String areaName;

    @ApiModelProperty(name = "pointName", value = "点位")
    @ExcelProperty(value = "点位名称", index = 3,width = 40)
    private String pointName;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    @ExcelProperty(value = "设备类型", ignore = true)
    private Integer deviceType;

    @ApiModelProperty(name = "deviceTypeName", value = "deviceTypeName")
    @ExcelProperty(value = "设备类型", index = 4)
    private String deviceTypeName;

    @ApiModelProperty(name = "ipAddr", value = "ip地址")
    @ExcelProperty(value = "ip地址", index = 5, width = 15)
    private String ipAddr;

    @ApiModelProperty(name = "alive", value = "1.在线；2.离线")
    @ExcelProperty(value = "1.在线；2.离线", ignore = true)
    private Integer alive;

    @ApiModelProperty(name = "aliveName", value = "1.在线；2.离线")
    @ExcelProperty(value = "在线状态",index = 6)
    private String aliveName;

    @ApiModelProperty(name = "enableStatus", value = "0，停用;1,启用")
    @ExcelProperty(value = "设备状态", ignore = true)
    private Integer enableStatus;

    @ApiModelProperty(name = "enableStatus", value = "0，停用;1,启用")
    @ExcelProperty(value = "设备状态",index = 7)
    private String enableStatusName;

    @ApiModelProperty(name = "brandName", value = "品牌")
    @ExcelProperty(value = "设备品牌",index = 8)
    private String brandName;

    @ApiModelProperty(name = "modelDes", value = "设备型号")
    @ExcelProperty(value = "设备型号",index = 9)
    private String modelDes;

    @ApiModelProperty(name = "aliveLastTime", value = "最后一次离线时间")
    @JSONField(name = "aliveLastTime", format = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "最后一次离线时间",index = 10,width = 18)
    private String  aliveLastTime;

    @ApiModelProperty(name = "longitude", value = "经度")
    @ExcelProperty(value = "经度",index = 11)
    private String longitude;

    @ApiModelProperty(name = "latitude", value = "纬度")
    @ExcelProperty(value = "纬度",index = 12)
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
