package com.zans.portal.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

@ApiModel(value="User返回值",description="User返回值")
@Data
public class UserRespVO {

    private Integer id;

//    @JSONField(name = "user_name")
    private String userName;

    @JSONField(name = "mobile")
    private String mobile;

//    @JSONField(name = "nick_name")
    private String nickName;

//    @JSONField(name = "lock_status")
    private Integer lockStatus;

    private Integer department;

//    @JSONField(name = "department_name")
    private String departmentName;

    private Integer enable;

//    @JSONField(name = "enable_name")
    private String enableName;

//    @JSONField(name = "lock_status_name")
    private String lockStatusName;

    private Integer role;

//    @JSONField(name = "is_admin")
    private Integer isAdmin;

//    @JSONField(name = "role_name")
    private String roleName;

    public void resetEnableName(Map<Object, String> map) {
        Integer status = this.enable;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setEnableName(name);
    }

    public void resetLockStatusName(Map<Object, String> map) {
        Integer status = this.lockStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setLockStatusName(name);
    }

    public void resetDepartName(Map<Object, String> map) {
        Integer status = this.department;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setDepartmentName(name);
    }
}
