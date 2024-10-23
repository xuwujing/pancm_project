package com.zans.wechat.message_push.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PushTemplate  implements Serializable {
    private String template_id;

    private String title;

    private String primary_industry;

    private String deputy_industry;

    private String content;

    private String example;
}
