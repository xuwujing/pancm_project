package com.zans.wechat.message_push.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class SendResult implements Serializable {
    private Integer errcode;

    private String errmsg;

    private String msgid;
}
