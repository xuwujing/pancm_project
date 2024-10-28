package com.zans.mms.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import com.zans.base.vo.SelectVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@ApiModel("用户信息导入excle模板实体")
@Data
public class ExcelUserInfoVO {

    @ApiModelProperty(value = "用户名称")
    @ExcelProperty(value = "用户名称", index = 0, validate = {"not_empty"})
    private String userName;

    @ApiModelProperty(value = "真实姓名")
    @ExcelProperty(value = "真实姓名", index = 1, validate = {"not_empty"})
    private String nickName;

    @ApiModelProperty(value = "电话")
    @ExcelProperty(value = "电话", index = 2, validate = {"not_empty"})
    private String mobile;

    @ApiModelProperty(value = "所属单位")
    @ExcelProperty(value = "所属单位", index = 3, validate = {"not_empty"})
    private String maintainCompanyName;

    @ApiModelProperty(value = "角色")
    @ExcelProperty(value = "角色", index = 4, validate = {"not_empty"})
    private String roleName;

    @ApiModelProperty(value = "微信登录")
    @ExcelProperty(value = "微信登录", index = 5, validate = {"not_empty"})
    private String wechatEnableName;


    @ApiModelProperty(value = "行号")
    @JSONField(name = "row_index")
    private Integer rowIndex;

    private Integer endIndex;

    private Integer wechatEnable;

    private String roleNum;

    private String maintainNum;


    public void resetMaintain(List<SelectVO> list) {
        if (StringUtils.isBlank(maintainCompanyName)){
            return;
        }
        if (list == null) {
            return;
        }

        for (SelectVO vo : list) {
            if (maintainCompanyName.equals(vo.getItemValue())) {
                this.maintainNum = (String) vo.getItemKey();
                break;
            }
        }
    }


    public void resetEnableStatus(List<SelectVO> list) {
        if (StringUtils.isBlank(wechatEnableName)){
            return;
        }
        if (list == null) {
            return;
        }

        for (SelectVO vo : list) {
            if (wechatEnableName.equals(vo.getItemValue())) {
                this.wechatEnable = (Integer) vo.getItemKey();
                break;
            }
        }
    }

    public void resetRole(List<SelectVO> list) {
        if (StringUtils.isBlank(roleName)){
            return;
        }
        if (list == null) {
            return;
        }

        for (SelectVO vo : list) {
            if (roleName.equals(vo.getItemValue())) {
                this.roleNum = (String) vo.getItemKey();
                break;
            }
        }
    }
}
