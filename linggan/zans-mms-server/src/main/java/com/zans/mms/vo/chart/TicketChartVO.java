package com.zans.mms.vo.chart;

import com.zans.base.vo.SelectVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单报表
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/24
 */
@Data
public class TicketChartVO implements Serializable {

	/**
	 * 今日新增
	 */
	private Integer todayAdd;

	/**
	 * 今日处理
	 */
	private Integer todayDeal;

	/**
	 * 累计未完成
	 */
	private Integer unCompletedCount;


	/**
	 * 工单列表
	 */
	private List<TicketChartRepVO> ticketChartRepVOList;

	private List<SelectVO> deviceTypeList;

	/**
	 * 初始化展示数据
	 */
	private List<TicketChartRepVO> recentTicketChartRepVOList;

}
