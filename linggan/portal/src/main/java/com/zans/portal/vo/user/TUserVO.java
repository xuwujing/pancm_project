package com.zans.portal.vo.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sys_user")
public class TUserVO extends BasePage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name")
//    @JSONField(name = "user_name")
    private String userName;

    private String mobile;

    private String password;

    /**
     * 用户角色
     */
    @Column(name = "role_name")
//    @JSONField(name = "role_name")
    private String roleName;


    @Column(name = "lock_status")
//    @JSONField(name = "lock_status")
    private Integer lockStatus;

    @Column(name = "salt")
    private String salt;

    /**
     * 所在部门
     */
    @Column(name = "department")
    @JSONField(name = "department")
    private Integer department;

    @Column(name = "enable")
    private Integer enable;

//    @Column(name = "delete_status")
    private Integer deleteStatus;

//    @Column(name = "is_admin")
    private Integer isAdmin;

    private String nowIp;

    private String loginTime;
}
