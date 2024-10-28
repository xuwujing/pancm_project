package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import com.zans.utils.RandomHelper;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_user")
@Data
@NoArgsConstructor
public class SysUser implements Serializable {
    @Id
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 0:否,1是
     */
    @Column(name = "is_admin")
    private Integer isAdmin;

    /**
     * 用户启用 1,启用；0，禁用; 2,锁定
     */
    private Integer enable;

    /*
     * 锁定状态 0未锁定 1锁定
     */
    @Column(name = "locak_status")
    private Integer lockStatus;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public SysUser(String userName) {
        this.userName = userName;
    }
}