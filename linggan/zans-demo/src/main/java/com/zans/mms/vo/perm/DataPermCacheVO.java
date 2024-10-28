package com.zans.mms.vo.perm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
* @Title: DataPermCacheVO
* @Description: 权限缓存VO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/9/21
*/
@Data
public class DataPermCacheVO {
    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "权限id")
    private Integer permId;

    @ApiModelProperty(value = "数据权限")
    private Integer dataPerm;

    @ApiModelProperty(value = "数据权限,逗号分割")
    private String dataPermDesc;
}
