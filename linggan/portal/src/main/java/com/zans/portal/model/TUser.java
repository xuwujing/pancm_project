package com.zans.portal.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.util.RandomHelper;
import com.zans.portal.config.GlobalConstants;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "sys_user")
public class TUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name")
//    @JSONField(name = "user_name")
    private String userName;

    @Column(name = "mobile")
    @JSONField(name = "mobile")
    private String mobile;

    private String password;

    /**
     * 真实姓名
     */
    @Column(name = "nick_name")
//    @JSONField(name = "nick_name")
    private String nickName;


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

    @Column(name = "delete_status")
    private Integer deleteStatus;

    @Column(name = "is_admin")
    private Integer isAdmin;

    private static final long serialVersionUID = 1L;

    public TUser removeSensitive() {
        this.password = null;
        this.salt = null;
        return this;
    }

    public TUser keepNormal() {
        this.setEnable(GlobalConstants.STATUS_ENABLE);
        this.setLockStatus(GlobalConstants.USER_LOCK_NONE);
        this.setIsAdmin(GlobalConstants.STATUS_DISABLE);
        return this;
    }

    /**
     * 用户的默认salt，随机数
     * @return
     */
    public static String getRandomSalt() {
        return RandomHelper.getRandomStr(12, "all");
    }
}
