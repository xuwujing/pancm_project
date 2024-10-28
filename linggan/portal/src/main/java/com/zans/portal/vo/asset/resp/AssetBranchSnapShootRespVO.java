package com.zans.portal.vo.asset.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pancm
 * @Title: portal
 * @Description: 快照响应实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/20
 */
@ApiModel(description="快照响应参数")
@Data
public class AssetBranchSnapShootRespVO {

    @ApiModelProperty(name = "id", value = "id")
    private Integer id;

    @ApiModelProperty(name = "snapShootTime", value = "快照时间")
    private String snapShootTime;

    @ApiModelProperty(name = "enable", value = "执行状态",hidden = true)
    private Integer enable;

    @ApiModelProperty(name = "execEnable", value = "执行状态",hidden = true)
    private Integer execEnable;

    @ApiModelProperty(name = "execStartTime", value = "执行开始时间",hidden = true)
    private String execStartTime;

    @ApiModelProperty(name = "execEndTime", value = "执行结束时间",hidden = true)
    private String execEndTime;

}
