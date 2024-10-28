package com.zans.mms.vo.ticket;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:小程序工单图表数据实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/22
 */
@Data
public class AppTicketChartVO implements Serializable {

	private List<String> Categories;

	private List<Map<String,Object>> series;
}
