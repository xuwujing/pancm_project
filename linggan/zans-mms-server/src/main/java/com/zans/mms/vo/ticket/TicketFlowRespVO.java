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
public class TicketFlowRespVO implements Serializable {
    private static final long serialVersionUID = -27477630671959084L;

    private Long ticketId;

    private String creator;

    private String roleNum;

    private String maintainNum;

    private String opName;

    private String description;

    private Integer deal;

    private Integer sort;

    private String checkTime;

    private String msg;


}
