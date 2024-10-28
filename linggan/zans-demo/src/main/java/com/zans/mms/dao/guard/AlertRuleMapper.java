package com.zans.mms.dao.guard;


import com.zans.mms.vo.alert.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: portal
 * @Description: 告警规则dao实现
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/27
 */
@Repository
public interface AlertRuleMapper {



	List<String> getRecordByKeywordValues(Map<String,Object> kvs);


	List<String> getRecordByIp(Map<String,Object> kvs);

	List<AlertReportDisRespVO> getAlertRecordType();

	List<AlertReportDisRespVO> getAlertRecordType2();

	List<AlertRecordRespVO> getAlertRecord(AlertRecordSearchVO reqVO);

	AlertRecordRespVO getAlertLastRecord();

	int updateIsRead(long id);



	/**
	 * @Author pancm
	 * @Description 分开查询，这里查询交换机相关信息的数据
	 * @Date  2020/11/19
	 * @Param [reqVO]
	 * @return java.util.List<com.zans.portal.vo.alert.AlertRecordRespVO>
	 **/
	List<AlertRecordRespVO> getAlertRecord2(AlertRecordSearchVO reqVO);

	/**
	 * @Author pancm
	 * @Description 通过mac查询radius_acct表
	 * @Date  2020/11/30
	 * @Param [mac]
	 * @return com.zans.portal.vo.alert.AlertRecordRespVO
	 **/
	AlertRecordRespVO getRecordByMac(String mac);


	int batchUpdateByIds(Map<String, Object> ids);


	List<AlertTypeRespVO> getAlertType(AlertTypeSearchVO reqVO);

	List<AlertIndexRespVO> getAlertIndex2(@Param("typeId") Integer typeId);

	AlertRecordRespVO getAlertRecordOriginalView(Long id);


	AlertRecordRespVO getAlertRecordView2(Long id);

	/**
	 * @Author pancm
	 * @Description 查看详情
	 * @Date  2020/12/9
	 * @Param [businessId]
	 * @return com.zans.portal.vo.alert.AlertDetailRespVO
	 **/
	AlertDetailRespVO getAlertRecordOriginalDetailView(String businessId);


	List<AlertIpClashRespVO> getAlertIpClash(String businessId);

	List<AlertLoopRespVO> getAlertLoop(String ip);

	 int  insertRecord(AlertRecordVO alertRecordVO);

	 int  insertOriginal(AlertOriginalVO alertOriginalVO);


	List<AlertRecordRespVO> getAlertRecordData();

	void saveAlertData(int ruleId, String businessId, String ipAddr, String mac, String msg);
}
