package com.zans.base.vo;

import lombok.Data;

@Data
public class UserSession {

    private Integer userId;
    private String userName;
    private String nickName;

    private String ip;

    public UserSession() {
    }

    public UserSession(Integer userId, String userName, String nickName) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
    }
}
