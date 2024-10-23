package com.zans.wechat.message_push.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class OpenidList implements Serializable {

    private Long total;

    private Long count;

    private Long next_openid;

    private List<String> data;
}
