package com.zans.portal.vo.asset.guardline.resp;

import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ExcelAssetGuardLineStatisticsVO {

    @ApiModelProperty(name = "assetGuardLineAssetId", value = "线路关联资产表id")
    @ExcelProperty(value = "线路关联资产表id", ignore = true)
    private Integer assetGuardLineAssetId;

    @ApiModelProperty(name = "assetGuardLineName", value = "线路名称")
    @ExcelProperty(value = "线路名称", index = 1, width = 15)
    private String  assetGuardLineName;

    @ApiModelProperty(name = "areaName", value = "所属辖区")
//    @ExcelProperty(value = "所属辖区", index = 2)   //7-15取消导出辖区
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
    @ExcelProperty(value = "经度",index = 11,width = 18)
    private String  longitude;

    @ApiModelProperty(name = "latitude", value = "纬度")
    @ExcelProperty(value = "纬度",index = 12,width = 18)
    private String  latitude;

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
