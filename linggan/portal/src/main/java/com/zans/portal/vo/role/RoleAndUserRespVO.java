package com.zans.portal.vo.role;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author xv
 * @since 2020/3/6 23:31
 */
@Data
public class RoleAndUserRespVO {

    @JSONField(name = "role_id")
    private Integer roleId;

    @JSONField(name = "role_name")
    private String roleName;

    @JSONField(name = "user_name")
    private String userName;

    @JSONField(name = "enable")
    private Integer enable;


}
