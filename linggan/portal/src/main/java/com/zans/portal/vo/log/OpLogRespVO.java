package com.zans.portal.vo.log;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel
@Data
public class OpLogRespVO {

    @ApiModelProperty(name = "id",value = "日志ID")
    private Integer id;

    @ApiModelProperty(name = "user_name",value = "用户名")
    @JSONField(name = "user_name")
    private String userName;

    @ApiModelProperty(name = "nick_name",value = "真实姓名")
    @JSONField(name = "nick_name")
    private String nickName;

    @ApiModelProperty(name = "role",value = "用户角色")
    private Integer role;

    @ApiModelProperty(name = "role_name",value = "用户角色名称")
    @JSONField(name = "role_name")
    private String roleName;

    @ApiModelProperty(name = "module",value = "操作模块")
    private Integer module;

    @ApiModelProperty(name = "module_name",value = "操作模块名称")
    @JSONField(name = "module_name")
    private String moduleName;

    @ApiModelProperty(name = "operation",value = "用户操作")
    private String operation;

    @ApiModelProperty(name = "content",value = "记录主键")
    private String content;

    @ApiModelProperty(name = "from_ip",value = "操作IP")
    @JSONField(name = "from_ip")
    private String fromIp;

    @ApiModelProperty(name = "result",value = "操作结果")
    private String result;

    @ApiModelProperty(name = "created_at",value = "操作时间")
    @JSONField(name = "created_at", format = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @ApiModelProperty(name = "content_map",value = "日志内容反序列化")
    @JSONField(name = "content_map")
    private Map<String, Object> contentMap;

    public void resetModuleNameByMap(Map<Object, String> map) {
        Integer status = this.module;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setModuleName(name);
    }

}
