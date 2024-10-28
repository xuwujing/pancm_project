package com.zans.mms.vo.chart;

import com.zans.mms.model.BaseVfs;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单返回实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/24
 */
@Data
public class TicketChartRepVO implements Serializable {

	/**
	 * 故障地址
	 */
	private String pointName;

	/**
	 * 设备类型名称
	 */
	private String deviceTypeName;

	/**
	 * 故障原因
	 */
	private String reason;

	/**
	 * 工单状态名称
	 */
	private String ticketStatusName;

	/**
	 * 故障来源名称
	 */
	private String issueSourceName;


	/**
	 * 发布时间
	 */
	private String createTime;

	/**
	 * 工单状态
	 */
	private Integer ticketStatus;

	/**
	 * 图片列表
	 */
	List<BaseVfs> baseVfsList;

	/**
	 * 工单id
	 */
	private Long id;

	private String adjunctId;

	private String lng;

	private String lat;



}
