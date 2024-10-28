package com.zans.portal.vo.network;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xv
 * @since 2020/6/4 16:38
 */
@ApiModel
@Data
public class ProjectSearchVO extends BasePage {

    @ApiModelProperty(value = "项目名称")
    private String name;

    @ApiModelProperty(value = "项目类型")
    private Integer type;

    @ApiModelProperty(value = "项目状态")
    private Integer status;

    @ApiModelProperty(value = "片区")
    private Integer region;

    @ApiModelProperty(value = "建设单位")
    private String contractor;

    @ApiModelProperty(value = "点位名称")
    private String point;

    @ApiModelProperty(value = "汇聚点IP")
    private String ip;

    @ApiModelProperty(value = "汇聚点名称")
    private String link;
}
