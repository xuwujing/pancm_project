package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.vo.alert.AlertRecordRespVO;
import com.zans.mms.vo.alert.AlertRecordSearchVO;

import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: portal
 * @Description: 告警查询service
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/27
 */
public interface IAlertRuleService {

	List<String> getRecordByKeywordValues(Map<String, Object> kvs);

	List<String> getRecordByIp(Map<String, Object> kvs);

	/**
	 * @Author pancm
	 * @Description 处置界面初始化
	 * @Date
	 * @Param [status]
	 * @return com.zans.base.vo.ApiResult
	 **/
	ApiResult  getAlertRecordInit(int status);

	List<SelectVO> getAlertType();

	List<SelectVO> getAlertIndex(Integer typeId);

	PageResult<AlertRecordRespVO> getAlertRecordPage(AlertRecordSearchVO reqVO);

	ApiResult getAlertRecordView(Long id, int status, int typeId);


	List<AlertRecordRespVO> getAlertRecordData();


	void saveAlertData(int ruleId, String businessId, String ipAddr, String mac, String msg);
}
