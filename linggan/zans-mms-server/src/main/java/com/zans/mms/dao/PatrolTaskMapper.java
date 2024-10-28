package com.zans.mms.dao;

import com.zans.mms.model.PatrolTask;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.patrol.*;
import com.zans.mms.vo.ticket.AppTicketCharReqVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface PatrolTaskMapper extends Mapper<PatrolTask> {
    int deleteByUniqueId(Long id);

    List<PatrolTaskResVO> getList(PatrolTaskQueryVO vo);

    PatrolTaskDetailResVO getViewById(Long id);

    List<TaskDetailVO> getPatrolResult(PatrolTaskQueryVO vo);

    PatrolTask getLastRecord(Long patrolSchemeId);

    List<PatrolLogRespVO> getPatrolLogList(PatrolLogQueryVO vo);

    List<PatrolPushMessageData> getPushPatrolData();

    /**
     * 获取首页上巡检的汇总数据
     * @param vo
     * @return
     */
    Map getPatrolTotal(PatrolTaskQueryVO vo);

    /**
     * 获取巡检完成数据
     * @return
     */
    List<PatrolFinshInfoVO> getPatrolFinshInfo();

    /**
     * 查询最近两次的巡检状态
     * @param pointId
     * @return
     */
    List<String> getRecentCheckResult(String pointId);

    /**
     * 汉口巡检数量查询
     * @param appTicketCharReqVO
     * @return
     */
    List<Integer> getCheckCount(AppTicketCharReqVO appTicketCharReqVO);

    List<Integer> getTimeOutCount(AppTicketCharReqVO appTicketCharReqVO);

    List<Integer> getBreakDownCount(AppTicketCharReqVO appTicketCharReqVO);

    BigDecimal getRate(AppTicketCharReqVO appTicketCharReqVO);

	void deleteCheckResult(@Param("id") Long id);
}
