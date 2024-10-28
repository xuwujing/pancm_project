package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;


/**
* @Title: LoginVO
* @Description:
* @Version:1.0.0
* @Since:jdk1.8
* @author beixing
* @Date  2021/8/16
**/
@Data
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 422123815503387089L;
    private String user;
    private String ip;
    private String url;
    private String driveName;
    private String linkName;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
