package com.zans.mms.dao;

import com.zans.mms.model.PatrolTask;
import com.zans.mms.model.PoManager;
import com.zans.mms.model.PoManagerLogs;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.po.*;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/21
 */
@Repository
public interface PoManagerDao extends Mapper<PoManager> {
	List<PoManagerRepVO> getList(PoManagerReqVO vo);

	List<PoExportVO> getPoExportData(PoManagerExportReqVO vo);

	List<PoManager> getAppList(PoManagerReqVO vo);

	PoManagerRepVO appView(Long id);

	void updateBase(PoManagerReqVO vo);

	List<PoManagerLogs> getLogs(Long id);


	List<CircleUnit> getAppPoManagerTotal(@Param("maintainNum") String maintainNum);

	void updateRepairPerson(PoManagerPushReqVO poManager);

	Integer check(PoManager poManager);

	Long getByCode(@Param("poCode") String poCode);

	Integer isNotExist(@Param("breakdownTime") String breakdownTime,@Param("originalProblemId") String originalProblemId);

	void updateDispatchTime(PoManager tempPoManager);

	Integer isDispatchTimeOut(@Param("id") Long id);

	Integer isMaintainTimeOut(@Param("id") Long id);

	void clearRepeatMark(@Param("id") Long id);

	List<Long> getIds(@Param("id") Long id);

	Integer getRepeatMark(@Param("id") Long id);

	List<Map<String, Object>> getReason(AppTicketCharReqVO appTicketCharReqVO);

	List<PoManagerDataVO> getPoTable(AppTicketCharReqVO appTicketCharReqVO);

	Integer getCount(AppTicketCharReqVO appTicketCharReqVO);

	Integer getMainCityCount(AppTicketCharReqVO appTicketCharReqVO);

	Integer getRemoteCityCount(AppTicketCharReqVO appTicketCharReqVO);

	List<PoManagerRepVO> getListByIds(@Param("ids") List<Long> ids);

	void relationTicket(@Param("id") Long id,@Param("ticketId") Long ticketId);
}
