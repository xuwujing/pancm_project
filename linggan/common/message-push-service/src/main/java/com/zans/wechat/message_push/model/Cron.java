package com.zans.wechat.message_push.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Cron implements Serializable {

    private Long id;

    private String cron;

    private String type;
}
