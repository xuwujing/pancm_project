package com.zans.mms.service.impl;

import com.zans.base.exception.BusinessException;
import com.zans.base.util.DateHelper;
import com.zans.base.util.MapBuilder;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.BaseVfsDao;
import com.zans.mms.dao.ChartDao;
import com.zans.mms.dao.SysConstantItemMapper;
import com.zans.mms.model.BaseVfs;
import com.zans.mms.service.IBaseDeviceTypeService;
import com.zans.mms.service.IChartService;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.vo.alert.AlertRecordRespVO;
import com.zans.mms.vo.alert.AlertRepVO;
import com.zans.mms.vo.chart.*;
import com.zans.mms.vo.patrol.PatrolLogRespVO;
import com.zans.mms.vo.ranking.QualityReqVO;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zans.base.config.GlobalConstants.SYS_DICT_KEY_PATROL_CHECK_RESULT;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:报表服务
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/9/23
 */
@Slf4j
@Service("chartService")
public class ChartServiceImpl implements IChartService {

	@Resource
	private ChartDao chartDao;

	@Autowired
	private BaseVfsDao baseVfsDao;

	@Autowired
	IConstantItemService constantItemService;

	@Autowired
	IBaseDeviceTypeService baseDeviceTypeService;

	@Autowired
	SysConstantItemMapper sysConstantItemMapper;

	@Override
	public ApiResult indexData(Integer type,String deviceType) {
		//传静态数据的情况
		return this.getIndexStaticData(type,deviceType);


	}




	/**
	 * 首页静态数据
	 * @return
	 */
	private ApiResult getIndexStaticData(Integer type,String deviceType) {
		if(StringHelper.isEmpty(deviceType)){
			deviceType="02";
		}
		//首页上面在线数据
		List<AssetOnlineChartVO> assetOnlineChartVOList =null;
		if(null==type||0==type){
			assetOnlineChartVOList=chartDao.getAssetOnline("assetOnline");
		}else{
			assetOnlineChartVOList=chartDao.getResultAssetOnline();
			//查在线率并计算
			List<OnlineResultVO> onlineResultVOList = chartDao.getOnlineResult();
			DecimalFormat df   = new DecimalFormat("######0.00");
			for(AssetOnlineChartVO assetOnlineChartVO : assetOnlineChartVOList){
				for(OnlineResultVO onlineResultVO : onlineResultVOList){
					if(assetOnlineChartVO.getDeviceType().equals(onlineResultVO.getDeviceType())){
						assetOnlineChartVO.setOnlineRateNum(onlineResultVO.getOnlineRate());
						assetOnlineChartVO.setOnlineRate(df.format(onlineResultVO.getOnlineRate().multiply(new BigDecimal(100)))+"%");
						break;
					}
				}
				if(StringHelper.isEmpty(assetOnlineChartVO.getOnlineRate())){
					assetOnlineChartVO.setOnlineRateNum(new BigDecimal(0.00));
					assetOnlineChartVO.setOnlineRate("0.00%");
				}
			}
		}

		//首页左边在线数据
		List<AssetOnlineChartVO> typeOnlineChartVOList = chartDao.getAssetOnline("typeOnline");
		//运维概览数据
		List<DataOverviewChartVO> overviewChartVOList = chartDao.getOverView("overView");
		//服务质量排名数据
		List<QualityRankingChartVO> qualityRankingChartVOList = chartDao.maxMaintenanceList();

		SystemNormalRateChartVO systemNormalRateChartVO = chartDao.getSystemNormal("systemNormal");

		List<CentralOfficeChartVO> centralOfficeChartVOList = chartDao.getCentralOffice("centralOffice");

		List<CommunicationNetworkChartVO> communicationNetworkChartVOList =null;
		if(null==type||0==type){
			communicationNetworkChartVOList=chartDao.getCommunicationNetwork("communicationNetwork");
		}else {
			communicationNetworkChartVOList=chartDao.getCommunicationNetworkResult();
		}


		//热力图返回值
		List<HeatMapChartVO> heatMapChartVOList = chartDao.getHeatMap(deviceType);
		Map<String,Object> result = MapBuilder.getBuilder()
				.put("assetOnline",assetOnlineChartVOList)
				.put("typeOnline",typeOnlineChartVOList)
				.put("overview",overviewChartVOList)
				.put("quality",qualityRankingChartVOList)
				.put("systemNoraml",systemNormalRateChartVO)
				.put("centralOffice",centralOfficeChartVOList)
				.put("communicationNetwork",communicationNetworkChartVOList)
				.put("heatMap",heatMapChartVOList)
				.build();
		return ApiResult.success(result);
	}


	/**
	 * 运维监控方法
	 * @return
	 */
	@Override
	public ApiResult ticketMonitoring(TicketChartReqVO ticketChartReqVO) {
		List<SelectVO> deviceTypeList = baseDeviceTypeService.findDeviceTypeToSelect();
		//查询今日新增 今日已处理 未处理总数
		Integer todayAdd = chartDao.getTodayAdd();
		//查询今日已处理
		Integer todayDeal = chartDao.getToDayDeal();
		//查询累计未完成
		Integer completedCount = chartDao.getUnCompletedCount();
		//查询当天所有的工单 或者从某一天开始轮播
		if(StringHelper.isEmpty(ticketChartReqVO.getStartDate())){
			ticketChartReqVO.setStartDate( DateHelper.getNow());
		}
		List<TicketChartRepVO> ticketChartVOList = chartDao.getTicketChartData(ticketChartReqVO);
		for(TicketChartRepVO ticketChartVO : ticketChartVOList){
			BaseVfs vfs = new BaseVfs();
			if(ticketChartVO.getTicketStatus()==1){
				vfs.setAdjunctId(ticketChartVO.getAdjunctId());
			}else{
				//查询维修完成的adjunctId
				String adjunctId = chartDao.getMaintainAdjunctId(ticketChartVO.getId());
				if(StringHelper.isEmpty(adjunctId)){
					adjunctId=ticketChartVO.getAdjunctId();
				}
				vfs.setAdjunctId(adjunctId);
			}
			if(!StringHelper.isEmpty(vfs.getAdjunctId())){
				List<BaseVfs> baseVfs = baseVfsDao.queryAll(vfs);
				ticketChartVO.setBaseVfsList(baseVfs);
			}

		}
		//初始化列表数据 防止页面上没有数据
		List<TicketChartRepVO> recentTicketChartVOList = chartDao.getRecentTicketChartData(ticketChartReqVO);
		for(TicketChartRepVO ticketChartVO : recentTicketChartVOList){
			BaseVfs vfs = new BaseVfs();
			if(ticketChartVO.getTicketStatus()==1){
				vfs.setAdjunctId(ticketChartVO.getAdjunctId());
			}else{
				//查询维修完成的adjunctId
				String adjunctId = chartDao.getMaintainAdjunctId(ticketChartVO.getId());
				if(StringHelper.isEmpty(adjunctId)){
					adjunctId=ticketChartVO.getAdjunctId();
				}
				vfs.setAdjunctId(adjunctId);
			}
			if(!StringHelper.isEmpty(vfs.getAdjunctId())){
				List<BaseVfs> baseVfs = baseVfsDao.queryAll(vfs);
				ticketChartVO.setBaseVfsList(baseVfs);
			}

		}

		TicketChartVO ticketChartVO = new TicketChartVO();
		ticketChartVO.setTodayAdd(todayAdd);
		ticketChartVO.setUnCompletedCount(completedCount);
		ticketChartVO.setTodayDeal(todayDeal);
		ticketChartVO.setTicketChartRepVOList(ticketChartVOList);
		ticketChartVO.setDeviceTypeList(deviceTypeList);
		ticketChartVO.setRecentTicketChartRepVOList(recentTicketChartVOList);
		//查巡检的数据
		return ApiResult.success(ticketChartVO);
	}

	/**
	 * 巡检监控数据
	 * @param ticketChartReqVO
	 * @return
	 */
	@Override
	public ApiResult patrolMonitoring(TicketChartReqVO ticketChartReqVO) {
		if(StringHelper.isEmpty(ticketChartReqVO.getStartDate())){
			ticketChartReqVO.setStartDate( DateHelper.getNow());
		}
		Integer todayCount=chartDao.getPatrolToday();
		Integer activeDiscovery = chartDao.getActiveDiscovery();


		List<SelectVO> checkResultList = constantItemService.findItemsByDict(SYS_DICT_KEY_PATROL_CHECK_RESULT);
		List<PatrolLogChartVO> respVOList = chartDao.getPatrolLogList(ticketChartReqVO);
		for (PatrolLogChartVO detailVO : respVOList) {
			//图片查询
			BaseVfs vfs;
			if (StringUtils.isNotBlank(detailVO.getAdjunctUuid())){
				vfs = new BaseVfs();
				vfs.setAdjunctId(detailVO.getAdjunctUuid());
				List<BaseVfs> baseVfs = baseVfsDao.queryAll(vfs);
				detailVO.setBaseVfsList(baseVfs);
			}
		}
		List<PatrolLogChartVO> recentData = chartDao.getRecentPatrolLogList(ticketChartReqVO);
		for (PatrolLogChartVO detailVO : recentData) {
			//图片查询
			BaseVfs vfs;
			if (StringUtils.isNotBlank(detailVO.getAdjunctUuid())){
				vfs = new BaseVfs();
				vfs.setAdjunctId(detailVO.getAdjunctUuid());
				List<BaseVfs> baseVfs = baseVfsDao.queryAll(vfs);
				detailVO.setBaseVfsList(baseVfs);
			}
		}
		Map<String, Object> result = MapBuilder.getBuilder()
				.put("checkResultList",checkResultList)
				.put("resp",respVOList)
				.put("recent",recentData)
				.put("today",todayCount)
				.put("activeDiscovery",activeDiscovery)
				.build();
		return ApiResult.success(result);
	}


	/**
	 * 维护单位评分
	 * @param ticketChartReqVO
	 * @return
	 */
	@Override
	public ApiResult maintainOrgRank(TicketChartReqVO ticketChartReqVO) {
		if(StringHelper.isEmpty(ticketChartReqVO.getStartDate())){
			return ApiResult.error("传参时间为空！");
		}
		List<QualityRankingChartVO> qualityRankingChartVOList = chartDao.getMaintainOrgRank(ticketChartReqVO.getStartDate());
		return ApiResult.success(qualityRankingChartVOList);
	}


	@Override
	public ApiResult qualityAssuranceUnit(TicketChartReqVO ticketChartReqVO) {
		if(StringHelper.isEmpty(ticketChartReqVO.getStartDate())){
			return ApiResult.error("传参时间为空！");
		}
		//当月的质保数据是否存在
		int qualityCount=chartDao.getQualityCount(1,ticketChartReqVO.getStartDate());
		if(qualityCount==0){
			//查询当前最大月份的数据
			List<ProjectOnlineRateChartVO> result = chartDao.getMAXQualityList(1);
			for(ProjectOnlineRateChartVO projectOnlineRateChartVO:result){
				QualityReqVO qualityReqVO = new QualityReqVO();
				BeanUtils.copyProperties(projectOnlineRateChartVO,qualityReqVO);
				qualityReqVO.setCurrentDate(ticketChartReqVO.getStartDate());
				qualityReqVO.setType(1);
				qualityReqVO.setCreator("lgwy");
				chartDao.insertQaulity(qualityReqVO);
			}
		}else{
			log.info("当月质保绩效考核数据已存在，不再生成！");
		}

		List<ProjectOnlineRateChartVO> projectOnlineRateChartVOS = chartDao.getProjectOnlineRate(1,ticketChartReqVO.getStartDate());

		return ApiResult.success(projectOnlineRateChartVOS);
	}

	@Override
	public ApiResult extensionProject(TicketChartReqVO ticketChartReqVO) {
		if(StringHelper.isEmpty(ticketChartReqVO.getStartDate())){
			return ApiResult.error("传参时间为空！");
		}
		//当月的质保数据是否存在
		int extensionCount=chartDao.getQualityCount(2,ticketChartReqVO.getStartDate());
		if(extensionCount==0){
			List<ProjectOnlineRateChartVO> result = chartDao.getMAXQualityList(2);
			for(ProjectOnlineRateChartVO projectOnlineRateChartVO:result){
				QualityReqVO qualityReqVO = new QualityReqVO();
				BeanUtils.copyProperties(projectOnlineRateChartVO,qualityReqVO);
				qualityReqVO.setCurrentDate(ticketChartReqVO.getStartDate());
				qualityReqVO.setType(2);
				qualityReqVO.setCreator("lgwy");
				chartDao.insertQaulity(qualityReqVO);
			}
		}else{
			log.info("当月拓建绩效考核数据已存在，不再生成！");
		}
		List<ProjectOnlineRateChartVO> projectOnlineRateChartVOS = chartDao.getProjectOnlineRate(2,ticketChartReqVO.getStartDate());

		return ApiResult.success(projectOnlineRateChartVOS);
	}

	@Override
	public ApiResult getAlertRecordPageTop(TicketChartReqVO ticketChartReqVO) {
		if(StringHelper.isEmpty(ticketChartReqVO.getStartDate())){
			ticketChartReqVO.setStartDate(DateHelper.getNow());
		}
		AlertRepVO alertRepVO =  new AlertRepVO();
		Connection mainConnection = this.getMainConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		if(null==mainConnection){
			throw  new BusinessException("主系统数据库连接失败！请检查配置！");
		}

		//告警查询sql语句 查询增量告警信息
		String mainSql=sysConstantItemMapper.getConstantValueByKey("main_sql");
		if(StringHelper.isEmpty(mainSql)){
			mainSql="  SELECT\n" +
					"        ao.`notice_info` AS noticeInfo,\n" +
					"        DATE_FORMAT(ao.`create_time`,'%Y-%m-%d %H:%i:%S') AS noticeTime,\n" +
					"        ao.ip_addr AS ipAddr,\n" +
					"        ao.area_name AS areaName,\n" +
					"        b.`alert_level` AS alertLevel\n" +
					"        FROM\n" +
					"        alert_rule_record a\n" +
					"        LEFT JOIN alert_rule_original ao ON ao.business_id = a.business_id\n" +
					"        LEFT JOIN alert_rule b ON a.`rule_id` = b.`id`\n" +
					"        WHERE ao.deal_status = 0 AND a.`rule_id` != 10\n";

		}
		String sql2=sysConstantItemMapper.getConstantValueByKey("main_sql2");
		if(StringHelper.isEmpty(sql2)){
			sql2=	" and ao.create_time > '%s'\n" +
					"        ORDER BY ao.create_time asc";
		}
		try {
			statement=mainConnection.prepareStatement(mainSql+String.format(sql2,ticketChartReqVO.getStartDate()));;
			rs=statement.executeQuery();
			List<AlertRecordRespVO> alertRecordRespVOList = new ArrayList<>();
			while(rs.next()){
				AlertRecordRespVO alertRecordRespVO = new AlertRecordRespVO();
				alertRecordRespVO.setAlertLevel(rs.getInt("alertLevel"));
				alertRecordRespVO.setAreaName(rs.getString("areaName"));
				alertRecordRespVO.setIpAddr(rs.getString("ipAddr"));
				alertRecordRespVO.setNoticeInfo(rs.getString("noticeInfo"));
				alertRecordRespVO.setNoticeTime(rs.getString("noticeTime"));
				alertRecordRespVOList.add(alertRecordRespVO);
			}
			alertRepVO.setAddData(alertRecordRespVOList);
		}catch (Exception e){
			log.error("增量数据查询失败！");
		}

		//告警查询sql语句 默认查询30条告警数据
		String rencent30=sysConstantItemMapper.getConstantValueByKey("rencent30");
		if(StringHelper.isEmpty(rencent30)){
			rencent30="select * from ( \n" +
					" SELECT\n" +
					"        ao.`notice_info` AS noticeInfo,\n" +
					"        DATE_FORMAT(ao.`create_time`,'%Y-%m-%d %H:%i:%S') AS noticeTime,\n" +
					"        ao.ip_addr AS ipAddr,\n" +
					"        ao.area_name AS areaName,\n" +
					"        b.`alert_level` AS alertLevel\n" +
					"        FROM\n" +
					"        alert_rule_record a\n" +
					"        LEFT JOIN alert_rule_original ao ON ao.business_id = a.business_id\n" +
					"        LEFT JOIN alert_rule b ON a.`rule_id` = b.`id`\n" +
					"        WHERE ao.deal_status = 0 AND a.`rule_id` != 10\n" +
					"        ORDER BY ao.create_time DESC limit 30) t\n" +
					"\t\t\t\torder by noticeTime asc";
		}
		statement=null;
		rs=null;
		try {
			statement=mainConnection.prepareStatement(rencent30);
			rs=statement.executeQuery();
			List<AlertRecordRespVO> alertRecordRespVOList = new ArrayList<>();
			while(rs.next()){
				AlertRecordRespVO alertRecordRespVO = new AlertRecordRespVO();
				alertRecordRespVO.setAlertLevel(rs.getInt("alertLevel"));
				alertRecordRespVO.setAreaName(rs.getString("areaName"));
				alertRecordRespVO.setIpAddr(rs.getString("ipAddr"));
				alertRecordRespVO.setNoticeInfo(rs.getString("noticeInfo"));
				alertRecordRespVO.setNoticeTime(rs.getString("noticeTime"));
				alertRecordRespVOList.add(alertRecordRespVO);
			}
			alertRepVO.setRecentData(alertRecordRespVOList);

		}catch (Exception e){
			log.error("最近数据查询失败！");
		}finally {
			close(rs,statement,mainConnection);
		}

		return ApiResult.success(alertRepVO);
	}

	private Connection getMainConnection() {
		String url = sysConstantItemMapper.getConstantValueByKey("mainjdbcurl");
		if(StringUtils.isBlank(url)){
			url="jdbc:mysql://192.168.9.52:3306/guard_scan?autoReconnect=true&failOverReadOnly=false&characterEncoding=utf-8&useUnicode=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true";
		}
		String name = sysConstantItemMapper.getConstantValueByKey("main_username");
		if(StringHelper.isEmpty(name)){
			name="guard_2";
		}
		String pwd = sysConstantItemMapper.getConstantValueByKey("main_pwd");
		if(StringHelper.isEmpty(name)){
			pwd ="Txsqldb@123456";
		}
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection= DriverManager.getConnection(url,name,pwd);
			return connection;
		}catch (Exception e){
			log.error("数据库连接失败！报错信息#{}",e);
			return null;

		}
	}

	@Override
	public ApiResult delayList() {
		List<DelayRepVO> delayList = chartDao.getDelayList("delay");
		return ApiResult.success(delayList);
	}

	/**
	 * 舆情返回值
	 * @param ticketChartReqVO
	 * @return
	 */
	@Override
	public ApiResult poManager(TicketChartReqVO ticketChartReqVO) {
		if(StringHelper.isEmpty(ticketChartReqVO.getStartDate())){
			ticketChartReqVO.setStartDate(DateHelper.getNow());
		}
		PoManagerChartVO poManagerChartVO= new PoManagerChartVO();
		//先查询今日新增数据
		Integer todayAddPoManager = chartDao.getTodayAddPoManager();
		//再查询所有已结案数据
		Integer dealCount = chartDao.getDealPoManagerCount();
		//再查询最近30条舆情数据
		List<PoManagerRepVO> recentPoManagerRepVOList = chartDao.getRecentPoManager();
		//当前时间查询出来的舆情数据
		List<PoManagerRepVO> currentPoManagerRepVOList = chartDao.getCurrentPoManager(ticketChartReqVO);
		poManagerChartVO.setTodayAdd(todayAddPoManager);
		poManagerChartVO.setDealCount(dealCount);
		poManagerChartVO.setRecentData(recentPoManagerRepVOList);
		poManagerChartVO.setCurrentData(currentPoManagerRepVOList);
		return ApiResult.success(poManagerChartVO);
	}

	public static void close(ResultSet rs, Statement stmt, Connection connection) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			log.error("数据连接关闭失败！", e);
		}
	}
}
