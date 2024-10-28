package com.zans.mms.dao;

import com.zans.mms.vo.chart.*;
import com.zans.mms.vo.patrol.PatrolLogRespVO;
import com.zans.mms.vo.ranking.MaintenanceReqVO;
import com.zans.mms.vo.ranking.QualityReqVO;
import com.zans.mms.vo.ranking.RankingQueryVO;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:报表查询持久层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/23
 */
@Mapper
public interface ChartDao {
	List<AssetOnlineChartVO> getAssetOnline(@Param("dictKey") String assetOnline);

	List<DataOverviewChartVO> getOverView(@Param("dictKey") String overView);

	List<QualityRankingChartVO> getQualityRanking(@Param("dictKey")String quality);

	SystemNormalRateChartVO getSystemNormal(@Param("dictKey")String systemNormal);

	List<CentralOfficeChartVO> getCentralOffice(@Param("dictKey")String centralOffice);

	List<CommunicationNetworkChartVO> getCommunicationNetwork(@Param("dictKey")String communicationNetwork);

	Integer getTodayAdd();

	Integer getUnCompletedCount();

	Integer getToDayDeal();

	List<HeatMapChartVO> getHeatMap(@Param("deviceType") String deviceType);

	List<AssetOnlineChartVO> getResultAssetOnline();

	List<OnlineResultVO> getOnlineResult();

	List<CommunicationNetworkChartVO> getCommunicationNetworkResult();

	List<TicketChartRepVO> getTicketChartData(TicketChartReqVO ticketChartReqVO);

	List<PatrolLogChartVO> getPatrolLogList(TicketChartReqVO ticketChartReqVO);

	String getMaintainAdjunctId(@Param("ticketId") Long id);

	Integer getPatrolToday();

	Integer getActiveDiscovery();

	List<QualityRankingChartVO> getMaintainOrgRank( @Param("startDate") String startDate);

	List<ProjectOnlineRateChartVO> getProjectOnlineRate(@Param("type")Integer type,@Param("startDate") String startDate);

	List<TicketChartRepVO> getRecentTicketChartData(TicketChartReqVO ticketChartReqVO);

	List<PatrolLogChartVO> getRecentPatrolLogList(TicketChartReqVO ticketChartReqVO);

	List<QualityRankingChartVO> getMaintenanceList(RankingQueryVO vo);

	void insertMaintence(MaintenanceReqVO maintenanceReqVO);

	void editMaintenance(MaintenanceReqVO vo);

	void delMaintenance(Long id);

	List<ProjectOnlineRateChartVO> getQualityList(RankingQueryVO vo);

	void editQuality(QualityReqVO vo);

	void delQuality(Long id,Integer type);

	void insertQaulity(QualityReqVO vo);

	Integer getQualityCount(@Param("type") Integer type,@Param("currentMonth") String currentMonth);

	List<ProjectOnlineRateChartVO> getMAXQualityList(@Param("type") Integer type);

	List<QualityRankingChartVO> maxMaintenanceList();

	List<DelayRepVO> getDelayList(@Param("dictKey") String delay);

	Integer getTodayAddPoManager();

	Integer getDealPoManagerCount();

	List<PoManagerRepVO> getRecentPoManager();

	List<PoManagerRepVO> getCurrentPoManager(TicketChartReqVO ticketChartReqVO);
}
