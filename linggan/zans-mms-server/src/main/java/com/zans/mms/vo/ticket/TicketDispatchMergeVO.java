package com.zans.mms.vo.ticket;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:派工单合并接受实体VO
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/10
 */
@Data
public class TicketDispatchMergeVO implements Serializable {

	private List<Long> ids;

	private String creator;
}
