package com.zans.wechat.message_push.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class TemplateModel implements Serializable {
    private String template_id;

    private String title;

    private String primary_industry;

    private String deputy_industry;

    private String content;

    private String example;
}
