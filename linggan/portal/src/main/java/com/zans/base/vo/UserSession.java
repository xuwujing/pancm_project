package com.zans.base.vo;

import lombok.Data;

@Data
public class UserSession {

    private Integer userId;
    private String userName;
    private String nickName;

    private String ip;

    private String traceId;


    public UserSession() {
    }

    public UserSession(Integer userId, String userName, String nickName,String traceId) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.traceId = traceId;
    }
}
