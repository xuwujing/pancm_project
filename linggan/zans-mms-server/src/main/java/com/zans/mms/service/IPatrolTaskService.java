package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.PatrolTask;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.patrol.AppPortalTaskTableRepVO;
import com.zans.mms.vo.patrol.PatrolLogQueryVO;
import com.zans.mms.vo.patrol.PatrolTaskDetailResVO;
import com.zans.mms.vo.patrol.PatrolTaskQueryVO;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;

import java.util.List;

/**
 * interface PatrolTaskservice
 *
 * @author
 */
public interface IPatrolTaskService extends BaseService<PatrolTask>{

     ApiResult getList(PatrolTaskQueryVO vo,UserSession userSession);

     PatrolTaskDetailResVO getViewById(Long id);



     int deleteById(Long id);

    ApiResult getView(PatrolTaskQueryVO vo);

    ApiResult getPatrolLogList(PatrolLogQueryVO vo, UserSession userSession);

    Boolean pushPatrolMessage();

    List<CircleUnit> getAppPatrolTotal(UserSession userSession);

    List<CircleUnit> getPcPatrolTotal(UserSession userSession);


    Boolean pushTicketMessageTask();

    /**
     * 巡检table
     * @param appTicketCharReqVO
     * @return
     */
    List<AppPortalTaskTableRepVO> getPoTable(AppTicketCharReqVO appTicketCharReqVO);


    /**
     * 删除一个巡检任务 及巡检任务下的所有巡检点位
     * @param id
     * @return
     */
	ApiResult deleteTask(Long id);

    void cleanPatrolResult(Integer cleanMonth);
}
