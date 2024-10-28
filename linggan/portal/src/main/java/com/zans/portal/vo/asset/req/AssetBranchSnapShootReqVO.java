package com.zans.portal.vo.asset.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author pancm
 * @Title: portal
 * @Description: 快照请求实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/20
 */
@ApiModel(description="快照请求参数")
@Data
public class AssetBranchSnapShootReqVO {

    @ApiModelProperty(name = "id", value = "id")
    private Integer id;

    @ApiModelProperty(name = "snapShootTime", value = "快照时间",required = true)
    @NotNull(message = "快照时间不能为空")
    private String snapShootTime;

    @ApiModelProperty(name = "enable", value = "启用状态",hidden = true)
    private Integer enable;

    @ApiModelProperty(name = "execEnable", value = "执行状态",hidden = true)
    private Integer execEnable;

    @ApiModelProperty(name = "execStartTime", value = "执行开始时间",hidden = true)
    private String execStartTime;

    @ApiModelProperty(name = "execEndTime", value = "执行结束时间",hidden = true)
    private String execEndTime;

    @ApiModelProperty(name = "creatorId", value = "创建人ID",hidden = true)
    private Integer creatorId;




}
