package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.PatrolAppCheckInfo;
import com.zans.mms.model.PatrolClockIn;
import com.zans.mms.model.PatrolTaskCheckResult;
import com.zans.mms.vo.patrol.*;

import java.util.List;

/** 
* @Title: IPatrolTaskCheckResultService 
* @Description:  
* @Version:1.0.0   
* @Since:jdk1.8  
* @author beiso
* @Date  2021/4/1 
**/ 
public interface IPatrolTaskCheckResultService extends BaseService<PatrolTaskCheckResult>{

    /**
    * @Author beiso
    * @Description  根据条件查询周围的巡检点位
    * @Date  2021/4/1
    * @Param 
    * @return 
    **/
    public List<PatrolDevicePointResVO> searchDevicePoint(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession);

    /**
     * @return 顶级聚合
     */
    public PatrolAggTopDataResVO aggregateData(UserSession userSession);

    public PatrolAppCheckInfo getCheckInfoWithApp(Integer pointId);

    public Integer updateClockIn(PatrolClockIn clockIn);


    /**
    * @Author beiming
    * @Description  app巡检加载全部点位
    * @Date  4/15/21
    * @Param
    * @return PatrolAllDevicePointResVO
    **/
    List<PatrolAllDevicePointResVO> appSearchAllDevicePoint(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession);

    /**
    * @Author beiming
    * @Description  获取巡检增量更新数据接口
    * @Date  4/15/21
    * @Param PatrolDevicePointReqVO
    * @return PatrolRefreshResVO
    **/
    PatrolRefreshResVO searchPatrolRefresh(PatrolDevicePointReqVO reqVO, UserSession userSession);

    /**
     * 获取巡检统计数据
     * @param reqVO
     * @return
     */
    PatrolRefreshResVO getPatrolStatistical(PatrolDevicePointReqVO reqVO, UserSession userSession);
    /**
     * @Author beiming
     * @Description  pc巡检加载全部点位
     * @Date  4/15/21
     * @Param
     * @return ApiResult
     **/

    ApiResult pcSearchAllDevicePoint(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession);
    /**
     * @Author beiming
     * @Description  pc巡检加载单个点位
     * @Date  4/15/21
     * @Param
     * @return ApiResult
     **/

    List<PatrolDevicePointResVO> pcSearchDevicePointByPointId(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession);

    /**
    * @Author beiming
    * @Description  app巡检点位详情
    * @Date  4/27/21
    * @Param
    * @return
    **/
    List<PatrolDevicePointResVO> searchDevicePointByPointId(PatrolDevicePointReqVO patrolDevicePointReqVO, UserSession userSession);
}