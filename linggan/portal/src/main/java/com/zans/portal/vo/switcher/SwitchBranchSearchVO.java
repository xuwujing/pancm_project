package com.zans.portal.vo.switcher;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class SwitchBranchSearchVO extends BasePage {

    @ApiModelProperty(name = "region", value = "区域ID")
    private Integer region;

    @ApiModelProperty(name = "swType", value = "交换机类型")
    private Integer swType;

    @ApiModelProperty(name = "online", value = "是否在线 1 在线 0 离线")
    private Integer online;

    @ApiModelProperty(name = "status", value = "状态 '0启用 1 停用" )
    private Integer status;

    @ApiModelProperty(name = "projectName",value = "项目名称")
    public String projectName;

    @ApiModelProperty(name = "ipAddr", value = "Ip")
    private String ipAddr;

    @ApiModelProperty(name = "pointName", value = "点位名称")
    private String pointName;

    @ApiModelProperty(name = "acceptance", value = "是否验收1是")
    private Integer acceptance;

    @ApiModelProperty(name = "dateRange", value = "验收时间")
    private List<String> dateRange;

    @ApiModelProperty(name = "interfaceRange", value = "接口数")
    private List<Integer> interfaceRange;

    @ApiModelProperty(name = "consBatch", value = "建设批次1 一期 2二期")
    private Integer consBatch;//

    //是否在线;1:在线；2；不在线
    public Integer getOnline(){
        return online;
    }

    public Integer getStatus() {
        if (status != null ){
            if (status == 1) {
                return 0;
            }else {
                return 1;
            }
        }
        return null;
    }
}
