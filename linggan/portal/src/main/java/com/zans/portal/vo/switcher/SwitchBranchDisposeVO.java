package com.zans.portal.vo.switcher;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
public class SwitchBranchDisposeVO {

    @ApiModelProperty(name = "id", value = "id")
    private Integer id;

    @ApiModelProperty(name = "ids", value = "ids")
    private List<Integer> ids;


    @ApiModelProperty(name = "status", value = "状态 0启用 1 停用")
    @NotNull(message = "状态")
    private Integer status;

    @ApiModelProperty(name = "reason", value = "处置原因")
    @NotNull(message = "处置原因")
    private String reason;


    public void addId(Integer id){
        if (ids == null){
            ids = new ArrayList<>();
        }
        ids.add(id);
    }

}
