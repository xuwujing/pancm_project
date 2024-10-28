package com.zans.mms.dao.mms;

import com.zans.mms.model.PatrolTask;
import com.zans.mms.vo.chart.CircleUnit;
import com.zans.mms.vo.patrol.*;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

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
}
