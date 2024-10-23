package com.zans.vo;

import com.alibaba.fastjson.JSONObject;

/**
 * @author pancm
 * @Title: asyn-client
 * @Description: 异步请求VO
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/14
 */

public class AsynRecordReqVO {

    /**来源项目编码  */
    private  String sourceProjectId;

    /** 目标项目编码 */
    private  String targetProjectId;

    private  String className;

    private  String methodName;

    /** 请求数据 */
    private JSONObject data;

    public String getSourceProjectId() {
        return sourceProjectId;
    }

    public void setSourceProjectId(String sourceProjectId) {
        this.sourceProjectId = sourceProjectId;
    }

    public String getTargetProjectId() {
        return targetProjectId;
    }

    public void setTargetProjectId(String targetProjectId) {
        this.targetProjectId = targetProjectId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
