package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.zans.utils.RandomHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * (SysUser)实体类
 *
 * @author beixing
 * @since 2021-01-16 16:41:09
 */
@ApiModel(value = "SysUserResp", description = "")
@Data
public class SysUserResp implements Serializable {
    private static final long serialVersionUID = 220498344842289800L;
    @Column(name = "id")
    @ApiModelProperty(value = "${column.comment}")
    private Integer id;
    /**
     * 用户名
     */
    @Column(name = "user_name")
    @ApiModelProperty(value = "用户名")
    private String userName;
    /**
     * 昵称
     */
    @Column(name = "nick_name")
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 手机号
     */
    @Column(name = "mobile")
    @ApiModelProperty(value = "手机号")
    private String mobile;
    /**
     * 账号
     */
    @Column(name = "account")
    @ApiModelProperty(value = "账号")
    private String account;
    /**
     * 密码
     */
    @Column(name = "password")
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * salt，用于MD5
     */
    @Column(name = "salt")
    @ApiModelProperty(value = "salt，用于MD5")
    private String salt;
    /**
     * 所属角色
     */
    @Column(name = "role_num")
    @ApiModelProperty(value = "所属角色")
    private String roleNum;
    /**
     * 所属单位
     */
    @Column(name = "maintain_num")
    @ApiModelProperty(value = "所属单位")
    private String maintainNum;
    /**
     * 0:否,1是
     */
    @Column(name = "is_admin")
    @ApiModelProperty(value = "0:否,1是")
    private Integer isAdmin;
    /**
     * 1,启用；0，禁用
     */
    @Column(name = "enable")
    @ApiModelProperty(value = "1,启用；0，禁用；2，锁定")
    private Integer enable;

    @Column(name = "create_time")
    @ApiModelProperty(value = "${column.comment}")
    @JSONField(name = "createTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "update_time")
    @JSONField(name = "updateTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "${column.comment}")
    private Date updateTime;


    /**
     * 1,启用；0，禁用
     */
    @Column(name = "wechat_enable")
    @ApiModelProperty(value = "1,启用；0，禁用")
    private Integer wechatEnable;


    /**
     * PC登录,1,启用；0，禁用;
     */
    @Column(name = "pc_enbale")
    @ApiModelProperty(value = "PC登录,1,启用；0，禁用;")
    private Integer pcEnable;
    /**
     * 微信推送,1,启用；0，禁用;
     */
    @Column(name = "wechat_push_enable")
    @ApiModelProperty(value = "微信推送,1,启用；0，禁用;")
    private Integer wechatPushEnable;

    @Column(name = "last_login_time")
    @ApiModelProperty(value = "${column.comment}")
    @JSONField(name = "lastLoginTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;



    @Column(name = "wechat_last_login_time")
    @ApiModelProperty(value = "${column.comment}")
     @JSONField(name = "wechatLastLoginTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date wechatLastLoginTime;
    /**
     * 1,启用；0，禁用
     */
    @Column(name = "wechat_name")
    @ApiModelProperty(value = "微信名称")
    private String wechatName;
    @Column(name = "creator")
    @ApiModelProperty(value = "创建人")
    private String creator;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 用户的默认salt，随机数
     * @return
     */
    public static String getRandomSalt() {
        return RandomHelper.getRandomStr(12, "all");
    }
}
