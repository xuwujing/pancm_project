package com.zans.mms.vo.chart;

import com.zans.mms.model.BaseVfs;
import lombok.Data;

import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/24
 */
@Data
public class PatrolLogChartVO {
	private Long id;
	private String areaId;
	private String pointName;
	private String pointId;
	private String pointCode;
	private String deviceType;
	private String checkResult;
	private String prevCheckResult;
	private String nickName;
	private String checkTime;
	private String adjunctUuid;
	private String orgId;

	private String checkRemark;
	List<BaseVfs> baseVfsList;

	private Long ticketId;

	private String ticketCode;

	private String lng;

	private String lat;

	private String checkResultName;

	private String deviceTypeName;
}
