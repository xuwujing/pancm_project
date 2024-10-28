package com.zans.mms.vo.perm;

import com.zans.mms.config.PermissionConstans;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataPermVO {
    @ApiModelProperty(value = "数据权限",name = " 数据权限")
    private Integer dataPerm;

//    @ApiModelProperty(value = "数据权限逗号分割",name = "数据权限逗号分割")
//    private String dataPermDesc;

    @ApiModelProperty(value = "数据权限list ",name = "数据权限list")
    private String[] dataPermList;

    public boolean selectAll(){
        int dataPermValue = 0;
        for (String s : dataPermList) {
            dataPermValue = dataPermValue + Integer.parseInt(s);
        }
        if ((PermissionConstans.PERM_ALL&dataPermValue) >= PermissionConstans.PERM_ALL ){
            //全部权限 1&dataPermValue >=1
            return true;
        }
        return false;
    }
}
