package com.zans.portal.vo.log;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponse;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author qiyi
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/6
 */
@ApiModel(value = "LogOperation", description = "")
@Data
@Table(name = "log_operation")
public class LogOperation52VO {

    private static final long serialVersionUID = 312168159839497256L;
    @Column(name = "id")
    @ApiModelProperty(value = "${column.comment}")
    private Long id;
    /**
     * 操作用户
     */
    @Column(name = "user_name")
    @ApiModelProperty(value = "操作用户")
    private String userName;
    /**
     * 操作平台
     */
    @Column(name = "op_platform")
    @ApiModelProperty(value = "操作平台")
    private String opPlatform;
    /**
     * 模块名称
     */
    @Column(name = "module")
    @ApiModelProperty(value = "模块名称")
    private String module;
    /**
     * 操作名称
     */
    @Column(name = "operation")
    @ApiModelProperty(value = "操作名称")
    private String operation;
    /**
     * 类名
     */
    @Column(name = "class_name")
    @ApiModelProperty(value = "类名")
    private String className;
    /**
     * 方法名
     */
    @Column(name = "method_name")
    @ApiModelProperty(value = "方法名")
    private String methodName;
    /**
     * 请求参数
     */
    @Column(name = "req_data")
    @ApiModelProperty(value = "请求参数")
    private String reqData;
    /**
     * 返回参数
     */
    @Column(name = "rep_data")
    @ApiModelProperty(value = "返回参数")
    private String repData;
    /**
     * 操作所在IP
     */
    @Column(name = "from_ip")
    @ApiModelProperty(value = "操作所在IP")
    private String fromIp;
    /**
     * 操作结果
     */
    @Column(name = "result")
    @ApiModelProperty(value = "操作结果")
    private String result;
    /**
     * 追踪ID
     */
    @Column(name = "trace_id")
    @ApiModelProperty(value = "追踪ID")
    private String traceId;
    /**
     * 操作时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "操作时间")
    private String createTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 用户角色
     */
    @Column(name = "role_name")
    @ApiModelProperty(value = "用户角色")
    private String roleName;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }


}
