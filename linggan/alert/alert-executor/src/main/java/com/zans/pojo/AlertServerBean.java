package com.zans.pojo;

import com.zans.commons.utils.MyTools;
import lombok.Data;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 告警服务实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
@Data
public class AlertServerBean {

    private Long server_id;
    private String server_name;
    private String server_ip;
    private Integer server_port;
    private String server_url;
    private Integer server_type;
    private String server_account;
    private String server_pwd;
    private Integer server_enable;
    private String server_desc;
    private String update_time;

    @Override
    public String toString() {
        return MyTools.toString(this);
    }
}
