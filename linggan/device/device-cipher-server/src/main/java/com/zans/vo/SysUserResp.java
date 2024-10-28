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
    @Column(name = "phone")
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**
     * 密码
     */
    @Column(name = "password")
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 0:否,1是
     */
    @Column(name = "is_admin")
    @ApiModelProperty(value = "0:否,1是")
    private Integer isAdmin;

    /**
     * 0:否,1是
     */
    @Column(name = "lock_status")
    @ApiModelProperty(value = "0:否,1是")
    private Integer lockStatus;
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
