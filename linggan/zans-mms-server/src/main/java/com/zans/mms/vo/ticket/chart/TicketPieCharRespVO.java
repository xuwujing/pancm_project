package com.zans.mms.vo.ticket.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description: 工单饼状图返回值实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/15
 */
@Data
public class TicketPieCharRespVO implements Serializable {


	private String name;

	private Integer value;
}
