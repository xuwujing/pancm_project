package com.zans.mms.vo.ticket;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/22
 */
@Data
public class TicketBatchExportVO implements Serializable {

	private List<Long> ids;


	private String type;
}
