package com.zans.vo;

import lombok.Data;

import javax.websocket.Session;

/**
 * @author beixing
 * @Title: leakproof-server
 * @Description: 过期session管理
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/28
 */
@Data
public class WebSocketSessionVO {

    private Long expireTime;

    private Session session;

    private String userName;

}