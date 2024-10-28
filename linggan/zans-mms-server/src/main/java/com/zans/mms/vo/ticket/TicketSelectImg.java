package com.zans.mms.vo.ticket;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单选择图片接收实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/27
 */
@Data
public class TicketSelectImg implements Serializable {

	/**
	 * 图片id
	 */
	private List<Long> ids;

	/**
	 * 图片排序
	 */
	private List<Integer> sorts;

	/**
	 * 附件id
	 */
	private String adjunctId;
}
