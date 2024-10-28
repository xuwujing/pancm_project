package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.PoManager;
import com.zans.mms.model.PoManagerLogs;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.po.*;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;
import com.zans.mms.vo.ticket.AppTicketChartVO;
import org.snmp4j.User;

import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情投诉管理逻辑层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
public interface IPoManagerService {

	ApiResult list(PoManagerReqVO vo);

	ApiResult create(PoManager poManager,UserSession userSession);

	ApiResult update(PoManager poManager,UserSession userSession);

	ApiResult delete(Long id);

	ApiResult view(Long id);


	String export(PoManagerExportReqVO vo, String fileName);

	ApiResult batchAddPo(String newFileName, String originName, UserSession userSession);

	/**
	 * 小程序列表页
	 * @param vo
	 * @return
	 */
	ApiResult appList(PoManagerReqVO vo);

	/**
	 * 小程序详情方法
	 * @param id
	 * @return
	 */
	ApiResult appView(Long  id);


	/**
	 * 图片字段维护
	 * @param vo
	 * @return
	 */
	ApiResult imgUpdate(PoManagerReqVO vo);

	/**
	 * 舆情日志
	 * @param id
	 * @return
	 */
	ApiResult getLogs(Long id);

	/**
	 * 保存日志
	 * @param poManagerLogs
	 * @return
	 */
	ApiResult saveLog(PoManagerLogs poManagerLogs,UserSession userSession);

	List<CircleUnit> getAppPoManagerTotal(UserSession userSession);

	/**
	 * 手动推送给维修人员
	 * @param poManager
	 * @param userSession
	 * @return
	 */
	ApiResult pushToRepair(PoManagerPushReqVO poManager, UserSession userSession);

	/**
	 * 获取维修人员
	 * @param areaId
	 * @return
	 */
	ApiResult getRepairPerson(Integer areaId);

	ApiResult check(PoManager poManager);

	/**
	 * 从名义平台中获取数据
	 */
	void getDataFormRemoteTask();

	void pushToAllDuty(PoManagerPushVO vo);

	void pushToDuty(PoManager poManager, UserSession user);

	void pushToClosingPerson(PoManager poManager, UserSession user);

	/**
	 * 重复标记
	 * @param poManager
	 */
	void repeatMark(PoManager poManager);

	void batchUpdate(PoManagerRepeatMarkVO vo, UserSession userSession);

	AppTicketChartVO getReason(AppTicketCharReqVO appTicketCharReqVO);

	/**
	 * 舆情表格
	 * @param appTicketCharReqVO
	 * @return
	 */
	List<PoManagerDataVO> getPoTable(AppTicketCharReqVO appTicketCharReqVO);

	Integer getCount(AppTicketCharReqVO appTicketCharReqVO);

	Integer getMainCityCount(AppTicketCharReqVO appTicketCharReqVO);

	Integer getRemoteCityCount(AppTicketCharReqVO appTicketCharReqVO);

}
