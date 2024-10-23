package com.zans.pojo;

import lombok.Data;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 任务实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/2
 */
@Data
public class JobBean {

    private Integer job_id;
    private Long task_id;
    private String job_name;
    private String job_data;
    private Integer enable;
    private String update_time;



}
