package com.zans.portal.vo.version;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: (SysVersionInfo)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 14:25:23
 */
@ApiModel(value = "SysVersionInfo", description = "")
@Data
public class SysVersionInfoRepVO  implements Serializable {
    private static final long serialVersionUID = 213164490075033458L;


    /**
     * 服务所属ip
     */
    @ApiModelProperty(value = "服务所属ip")
    private String serverIp;
    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址")
    private String serverUrl;



    private String app_name;
    private String git_commit;
    private String git_branch;
    private String profile;
    private String build_time;
    private String version;
    private String db_name;
    private String db_version;
    private String db_remark;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
