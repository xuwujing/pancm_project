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
    private String mobile;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * salt，用于MD5
     */
    private String salt;

    /**
     * 所属角色
     */
    @Column(name = "role_num")
    private String roleNum;

    /**
     * 所属单位
     */
    @Column(name = "maintain_num")
    private String maintainNum;

    /**
     * 0:否,1是
     */
    @Column(name = "is_admin")
    private Integer isAdmin;

    /**
     * 用户启用 1,启用；0，禁用; 2,锁定
     */
    private Integer enable;

    /**
     * 微信登录,1,启用；0，禁用; 
     */
    @Column(name = "wechat_enable")
    private Integer wechatEnable;

    /**
     * PC登录,1,启用；0，禁用;
     */
    @Column(name = "pc_enable")
    private Integer pcEnable;

    /**
     * 微信推送,1,启用；0，禁用;
     */
    @Column(name = "wechat_push_enable")
    private Integer wechatPushEnable;

    /**
     * pc最后一次登录时间
     */
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * 微信最后一次登录时间
     */
    @Column(name = "wechat_last_login_time")
    private Date wechatLastLoginTime;

    /**
     * 微信号
     */
    @Column(name = "wechat_name")
    private String wechatName;

    /**
     * 创建人
     */
    private String creator;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新gis点位权限字段
     */
    @Column(name = "check_gis")
    private Integer checkGis;

    /**
     * app可以看到辖区权限，多个逗号分割
     */
    @Column(name = "area_id_str")
    private String areaIdStr;

    /**
     * 部署项目编号
     */
    @Column(name = "project_id")
    private String projectId;

    /**
     * 用户的默认salt，随机数
     * @return
     */
    public static String getRandomSalt() {
        return RandomHelper.getRandomStr(12, "all");
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public SysUser(String userName) {
        this.userName = userName;
    }
}