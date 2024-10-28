package com.zans.portal.vo.role;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.vo.perm.PermissionRespVO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xv
 * @since 2020/3/6 23:31
 */
@Data
public class RoleRespVO {

//    @JSONField(name = "role_id")
    private Integer roleId;

//    @JSONField(name = "role_name")
    private String roleName;

//    @JSONField(name = "role_desc")
    private String roleDesc;

    @JSONField(name = "enable")
    private Integer enable;

//    @JSONField(name = "enable_name")
    private String enableName;

//    @JSONField(name = "perm_list")
    private List<PermissionRespVO> permList;

//    @JSONField(name = "checked_keys")
    private List<Integer> checkedPermission;

    public void resetEnableNameByMap(Map<Object, String> map) {
        Integer status = this.getEnable();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setEnableName(name);
    }

}
