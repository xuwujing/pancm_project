package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.vo.ranking.MaintenanceReqVO;
import com.zans.mms.vo.ranking.QualityReqVO;
import com.zans.mms.vo.ranking.RankingQueryVO;

import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:绩效考核数据录入相关
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/8
 */
public interface IRankingService {

	ApiResult maintenanceList(RankingQueryVO vo);

	ApiResult addMaintenance(List<MaintenanceReqVO> vo, UserSession userSession);

	ApiResult editMaintenance(MaintenanceReqVO vo);

	ApiResult delMaintenance(MaintenanceReqVO vo);

	ApiResult addOneMaintenance(MaintenanceReqVO vo, UserSession user);

	ApiResult qualityList(RankingQueryVO vo);

	ApiResult editQuality(QualityReqVO vo);

	ApiResult delQuality(QualityReqVO vo);

	ApiResult addQuality(QualityReqVO vo, UserSession user);

	void generateRankingData();

	ApiResult maxMaintenanceList();
}
