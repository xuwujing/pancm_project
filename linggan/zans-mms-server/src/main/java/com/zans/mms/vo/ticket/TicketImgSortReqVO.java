package com.zans.mms.vo.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单图片排序接收实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/27
 */
@Data
@AllArgsConstructor
public class TicketImgSortReqVO implements Serializable {

	private Long id;

	private Integer sort;
}
