package com.zans.portal.vo.asset.map;

import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "资产地图汇聚返回体")
@Data
public class AssetMapConvergeRespVO {

    @ApiModelProperty(name = "pointList",value = "地图点集合list")
    List<AssetMapRespVO> pointList;

    @ApiModelProperty(name = "convergeList",value = "汇聚点集合list")
    List<ConvergeVO> convergeList;

    @ApiModelProperty(name = "reqVO",value = "请求的参数")
    AssetMapInitReqVO  reqVO;
}
