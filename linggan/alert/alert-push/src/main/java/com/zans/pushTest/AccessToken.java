package com.zans.pushTest;

import lombok.Data;

/**
 * @author pancm
 * @Title: alert-push
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/11/2
 */
@Data
public class AccessToken {
    //获取到的access_token字符串
    private String access_token;
    //有效时间（2h，7200s）
    private int expires_in;
}
