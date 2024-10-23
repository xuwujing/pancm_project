package com.zans.wechat.message_push.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class Config implements Serializable {

    private Long id;

    private Long groupId;

    private String name;

    private String appid;

    private String sercet;

    private Integer type;

    private String nextOpenid;

    private Integer intervalPullTime;

    private Integer enableStatus;

    private String creator;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;



}
