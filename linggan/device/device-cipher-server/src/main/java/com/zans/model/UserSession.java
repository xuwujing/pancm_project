package com.zans.model;

import lombok.Data;

@Data
public class UserSession {

    private Integer userId;
    private String userName;
    private String nickName;

    private String ip;

    private String traceId;

    private String roleId;

    private String orgId;

    private String areaIdStr;

    private Integer isAdmin;


    public UserSession() {
    }

    public UserSession(Integer userId, String userName, String nickName, String ip, String traceId, String roleId, String orgId,String areaIdStr) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.ip = ip;
        this.traceId = traceId;
        this.roleId = roleId;
        this.orgId = orgId;
        this.areaIdStr = areaIdStr;
    }
}
