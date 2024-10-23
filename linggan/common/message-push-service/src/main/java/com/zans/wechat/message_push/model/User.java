package com.zans.wechat.message_push.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class User implements Serializable  {
    private Long id;

    private String appid;

    private String openid;

    private String unionid;

    private String nickname;

    private int sex;

    private String language;

    private String city;

    private String province;

    private String country;

    private String headimgurl;

    private Integer enable_status;

    private Integer subscribe;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date subscribe_time;

    private String subscribe_scene;

    private Integer enableStatus;

   private String creator;

   @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date create_time;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date update_time;

    private String remark;

    private Integer groupid;

    private List tagid_list;

    private String qr_scene;

    private String qr_scene_str;

}
