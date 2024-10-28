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
public class EdrVO implements Serializable {
    private static final long serialVersionUID = 422123815503387089L;

    /** 路径*/
    private String path;
    /** 程序名称 */
    private String name;
    /** 执行命令，start或stop*/
    private String cmd;




    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
