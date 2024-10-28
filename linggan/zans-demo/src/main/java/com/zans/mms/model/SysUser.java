package com.zans.mms.model;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.util.RandomHelper;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (SysUser)实体类
 *
 * @author beixing
 * @since 2021-03-15 15:01:54
 */
@ApiModel(value = "SysUser", description = "")
@Data
@Table(name = "sys_user")
public class SysUser extends BasePage implements Serializable {
    private static final long serialVersionUID = 886716921354902989L;
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
     * 用户启用 1,启用；0，禁用; 2,锁定
     */
    @Column(name = "enable")
    @ApiModelProperty(value = "用户启用 1,启用；0，禁用; 2,锁定")
    private Integer enable;
    /**
     * 微信登录,1,启用；0，禁用;
     */
    @Column(name = "wechat_enable")
    @ApiModelProperty(value = "微信登录,1,启用；0，禁用; ")
    private Integer wechatEnable;
    /**
     * PC登录,1,启用；0，禁用;
     */
    @Column(name = "pc_enable")
    @ApiModelProperty(value = "PC登录,1,启用；0，禁用;")
    private Integer pcEnable;
    /**
     * 微信推送,1,启用；0，禁用;
     */
    @Column(name = "wechat_push_enable")
    @ApiModelProperty(value = "微信推送,1,启用；0，禁用;")
    private Integer wechatPushEnable;
    /**
     * pc最后一次登录时间
     */
    @Column(name = "last_login_time")
    @ApiModelProperty(value = "pc最后一次登录时间")
    private Date lastLoginTime;
    /**
     * 微信最后一次登录时间
     */
    @Column(name = "wechat_last_login_time")
    @ApiModelProperty(value = "微信最后一次登录时间")
    private Date wechatLastLoginTime;
    /**
     * 微信号
     */
    @Column(name = "wechat_name")
    @ApiModelProperty(value = "微信号")
    private String wechatName;
    /**
     * 创建人
     */
    @Column(name = "creator")
    @ApiModelProperty(value = "创建人")
    private String creator;
    @Column(name = "create_time")
    @ApiModelProperty(value = "${column.comment}")
    private String createTime;
    @Column(name = "update_time")
    @ApiModelProperty(value = "${column.comment}")
    private String updateTime;


    @ApiModelProperty(name = "area_num")
    private String areaNum;

    private String maintainName;

    /**
     * 更新gis点位权限字段 1.有权限；0.无权限;
     */
    @Column(name = "check_gis")
    @ApiModelProperty(value = "1.有权限；0.无权限;")
    private Integer checkGis;

    /**
     * app可以看到辖区权限，多个逗号分割
     */
    @Column(name = "area_id_str")
    @ApiModelProperty(value = "app可以看到辖区权限，多个逗号分割")
    private String areaIdStr;

    @Column(name = "project_id")
    @ApiModelProperty(value = "projectId")
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

}
