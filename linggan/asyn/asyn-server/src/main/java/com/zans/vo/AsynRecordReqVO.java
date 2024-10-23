package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author pancm
 * @Title: asyn-client
 * @Description: 异步请求VO
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/14
 */
@Data
public class AsynRecordReqVO {

    /**来源项目编码  */
    private  String sourceProjectId;

    /** 目标项目编码 */
    private  String targetProjectId;

    private  String className;

    private  String methodName;

    /** 请求数据 */
    private JSONObject data;



    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
