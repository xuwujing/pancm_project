package com.zans.portal.vo.asset.map;

import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "汇聚点VO")
@Data
public class ConvergeVO {

    @ApiModelProperty(name = "latitude",value = "汇聚点经度")
    private String latitude;

    @ApiModelProperty(name = "longitude",value = "汇聚点纬度")
    private String longitude;

    @ApiModelProperty(name = "convergeNumber",value = "汇聚点数量")
    private Integer convergeNumber;



    @ApiModelProperty(name = "leftLatitude",value = "左下角（西南角）经度")
    private String leftLatitude;
    @ApiModelProperty(name = "leftLongitude",value = "左下角（西南角）纬度")
    private String leftLongitude;
    @ApiModelProperty(name = "rightLatitude",value = "右上角（东北角）经度")
    private String rightLatitude;
    @ApiModelProperty(name = "rightLongitude",value = "右上角（东北角）纬度")
    private String rightLongitude;

    @ApiModelProperty(name = "onlineNumber",value = "在线数量")
    private Integer onlineNumber;

    @ApiModelProperty(name = "offlineNumber",value = "离线数量")
    private Integer offlineNumber;

    /**
    * @Author beiming
    * @Description  设置在线 、离线数量
    * @Date  5/10/21
    * @Param
    * @return
    **/
    public void resetOnlineOfflineNumber(List<AssetMapRespVO> mapList) {
        long onlineNum = mapList.stream().filter(assetMapRespVO -> "1".equals(assetMapRespVO.getDeviceStatus())).count();
        long offlineNum = mapList.stream().filter(assetMapRespVO -> "2".equals(assetMapRespVO.getDeviceStatus())).count();
        this.setOfflineNumber((int)offlineNum);
        this.setOnlineNumber((int)onlineNum);
    }
}
