package com.zans.mms.vo.ticket;

import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: zans-mms-server
 * @Description: 工作流待办实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/11
 */
@Data
public class TicketWorkFlowRespVO implements Serializable {
    private static final long serialVersionUID = -27477630671959084L;


    private String taskName;

    private String businessStatusName;

    private String businessStatusCode;



}
