package com.zans.mms.vo.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情返回值实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/12
 */
@Data
public class PoManagerRepVO implements Serializable {

	/**
	 * 故障时间
	 */
	private String  breakdownTime;

	/**
	 * 故障描述
	 */
	private String problemDescription;

	/**
	 * 事件来源
	 */
	private String eventSource;
}
