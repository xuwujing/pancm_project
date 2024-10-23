package com.zans.wechat.message_push.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 记录请求接口返回的AccessToken
 */
@Data
public class AccessToken implements Serializable  {

    private String access_token;

    private Long expires_in;

}
