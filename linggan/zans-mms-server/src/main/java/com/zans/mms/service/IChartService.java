package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.vo.alert.AlertRecordRespVO;
import com.zans.mms.vo.chart.TicketChartReqVO;

import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:报表服务逻辑层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/23
 */
public interface IChartService {
	ApiResult indexData(Integer type,String deviceType);

	ApiResult ticketMonitoring(TicketChartReqVO ticketChartReqVO);

	ApiResult patrolMonitoring(TicketChartReqVO ticketChartReqVO);

	ApiResult maintainOrgRank(TicketChartReqVO ticketChartReqVO);

	ApiResult qualityAssuranceUnit(TicketChartReqVO ticketChartReqVO);

	ApiResult extensionProject(TicketChartReqVO ticketChartReqVO);

	ApiResult getAlertRecordPageTop(TicketChartReqVO ticketChartReqVO);

	ApiResult delayList();

	ApiResult poManager(TicketChartReqVO ticketChartReqVO);
}
