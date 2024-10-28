package com.zans.mms.util;

import com.zans.base.vo.UserSession;
import com.zans.mms.model.LogOperation;


import java.util.Date;

public class LogBuilder {

    private Integer userId;

    private String userName;

    private String nickName;

    private String module;

    private String operation;

    private String content;

    private String fromIp;

    private String result;


    private String traceId;



    public LogBuilder() {

    }

    public LogBuilder session(UserSession userSession) {
        if (userSession != null) {
            this.userId = userSession.getUserId();
            this.userName = userSession.getUserName();
            this.nickName = userSession.getNickName();
            this.fromIp = userSession.getIp();
            this.traceId = userSession.getTraceId();
        }
        return this;
    }

    public LogBuilder userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public LogBuilder userName(String userName) {
        this.userName = userName;
        return this;
    }

    public LogBuilder nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public LogBuilder module(String module) {
        this.module = module;
        return this;
    }

    public LogBuilder operation(String operation) {
        this.operation = operation;
        return this;
    }

    public LogBuilder content(String content) {
        this.content = content;
        return this;
    }

    public LogBuilder content(Integer i) {
        if (i == null) {
            this.content = "";
        } else {
            this.content = i + "";
        }

        return this;
    }

    public LogBuilder fromIp(String fromIp) {
        this.fromIp = fromIp;
        return this;
    }

    public LogBuilder result(String result) {
        this.result = result;
        return this;
    }

    public LogOperation build() {
        return new LogOperation(this.userName, this.fromIp, this.module, this.operation, this.content, this.result, new Date(),this.traceId);
    }
}
