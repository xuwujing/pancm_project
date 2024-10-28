package com.zans.mms.vo.alert;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:告警返回值
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/11
 */
@Data
public class AlertRepVO implements Serializable {

	/**
	 * 最近30条数据
	 */
	private List<AlertRecordRespVO> recentData;

	/**
	 * 增量数据
	 */
	private List<AlertRecordRespVO> addData;
}