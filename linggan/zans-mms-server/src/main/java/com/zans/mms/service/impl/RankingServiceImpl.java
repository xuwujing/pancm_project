package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.util.StringHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.dao.ChartDao;
import com.zans.mms.service.IRankingService;
import com.zans.mms.vo.chart.ProjectOnlineRateChartVO;
import com.zans.mms.vo.chart.QualityRankingChartVO;
import com.zans.mms.vo.ranking.MaintenanceReqVO;
import com.zans.mms.vo.ranking.QualityReqVO;
import com.zans.mms.vo.ranking.RankingQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:绩效考核数据录入逻辑层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/8
 */
@Slf4j
@Service("rankingService")
public class RankingServiceImpl implements IRankingService {



	@Resource
	ChartDao chartDao;

	@Override
	public ApiResult maintenanceList(RankingQueryVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<QualityRankingChartVO> result = chartDao.getMaintenanceList(vo);
		return ApiResult.success(new PageResult<QualityRankingChartVO>(page.getTotal(), result, pageSize, pageNum));
	}

	@Override
	public ApiResult maxMaintenanceList() {
		List<QualityRankingChartVO> result = chartDao.maxMaintenanceList();
		return ApiResult.success(result);
	}

	/**
	 * 新增绩效考核数据
	 * @param vo
	 * @param userSession
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public ApiResult addMaintenance(List<MaintenanceReqVO> vo, UserSession userSession) {
		if(StringHelper.isEmpty(vo)){
			return ApiResult.error("数据为空！");
		}
		for(MaintenanceReqVO maintenanceReqVO:vo){
			maintenanceReqVO.setCreator(userSession.getUserName());
			//计算分数
			Double score=100.00D-Double.valueOf(maintenanceReqVO.getDeductPoints());
			maintenanceReqVO.setScore(score.toString());
			chartDao.insertMaintence(maintenanceReqVO);
		}
		return ApiResult.success();
	}

	@Override
	public ApiResult editMaintenance(MaintenanceReqVO vo) {
		Double score=100.00D-Double.valueOf(vo.getDeductPoints());
		vo.setScore(score.toString());
		chartDao.editMaintenance(vo);
		return ApiResult.success();
	}

	@Override
	public ApiResult delMaintenance(MaintenanceReqVO vo) {
		chartDao.delMaintenance(vo.getId());
		return ApiResult.success();
	}

	@Override
	public ApiResult addOneMaintenance(MaintenanceReqVO vo, UserSession user) {
		vo.setCreator(user.getUserName());
		//计算分数
		Double score=100.00D-Double.valueOf(vo.getDeductPoints());
		vo.setScore(score.toString());
		chartDao.insertMaintence(vo);
		return ApiResult.success();
	}


	@Override
	public ApiResult qualityList(RankingQueryVO vo) {
		int pageNum = vo.getPageNum();
		int pageSize = vo.getPageSize();
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<ProjectOnlineRateChartVO> result = chartDao.getQualityList(vo);
		return ApiResult.success(new PageResult<ProjectOnlineRateChartVO>(page.getTotal(), result, pageSize, pageNum));
	}

	@Override
	public ApiResult editQuality(QualityReqVO vo) {

		chartDao.editQuality(vo);
		return ApiResult.success();
	}

	@Override
	public ApiResult delQuality(QualityReqVO vo) {
		chartDao.delQuality(vo.getId(),vo.getType());
		return ApiResult.success();
	}

	@Override
	public ApiResult addQuality(QualityReqVO vo, UserSession user) {
		vo.setCreator(user.getUserName());
		chartDao.insertQaulity(vo);
		return ApiResult.success();
	}

	/**
	 * 定时生成质保单位和扩建项目的数据
	 */
	@Override
	public void generateRankingData() {
		log.info("当月绩效考核数据生成开始！");
		//查询当月是否有数据 如果没有数据 则生成 然后插入
		String currentMonth = new SimpleDateFormat("yyyy-MM").format(new Date());
		//当月的质保数据是否存在
		int qualityCount=chartDao.getQualityCount(1,currentMonth);
		int extensionCount=chartDao.getQualityCount(2,currentMonth);
		if(qualityCount==0){
			//查询当前最大月份的数据
			List<ProjectOnlineRateChartVO> result = chartDao.getMAXQualityList(1);
			for(ProjectOnlineRateChartVO projectOnlineRateChartVO:result){
				QualityReqVO qualityReqVO = new QualityReqVO();
				BeanUtils.copyProperties(projectOnlineRateChartVO,qualityReqVO);
				qualityReqVO.setCurrentDate(currentMonth);
				qualityReqVO.setType(1);
				qualityReqVO.setCreator("lgwy");
				chartDao.insertQaulity(qualityReqVO);
			}
		}else{
			log.info("当月质保绩效考核数据已存在，不再生成！");
		}
		if(extensionCount==0){
			List<ProjectOnlineRateChartVO> result = chartDao.getMAXQualityList(2);
			for(ProjectOnlineRateChartVO projectOnlineRateChartVO:result){
				QualityReqVO qualityReqVO = new QualityReqVO();
				BeanUtils.copyProperties(projectOnlineRateChartVO,qualityReqVO);
				qualityReqVO.setCurrentDate(currentMonth);
				qualityReqVO.setType(2);
				qualityReqVO.setCreator("lgwy");
				chartDao.insertQaulity(qualityReqVO);
			}
		}else{
			log.info("当月拓建绩效考核数据已存在，不再生成！");
		}

		log.info("当月绩效考核数据生成结束！");

	}
}
