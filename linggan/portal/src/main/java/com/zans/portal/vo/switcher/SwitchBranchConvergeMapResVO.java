package com.zans.portal.vo.switcher;

import com.zans.portal.vo.asset.resp.AssetMapRespVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel
@Data
public class SwitchBranchConvergeMapResVO {

    @ApiModelProperty(name = "pointList",value = "地图点集合list")
    List<SwitchBranchResVO> pointList;

    @ApiModelProperty(name = "convergeList",value = "汇聚点集合list")
    List  convergeList;

    @ApiModelProperty(name = "reqVO",value = "请求的参数")
    SwitchBranchMapInitVO  reqVO;


}
