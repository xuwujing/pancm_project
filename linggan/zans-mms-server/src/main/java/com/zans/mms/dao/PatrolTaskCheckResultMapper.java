package com.zans.mms.dao;

import com.zans.mms.model.PatrolAggData;
import com.zans.mms.model.PatrolAppCheckInfo;
import com.zans.mms.model.PatrolClockIn;
import com.zans.mms.model.PatrolTaskCheckResult;
import com.zans.mms.vo.patrol.PatrolAllDevicePointResVO;
import com.zans.mms.vo.patrol.PatrolDevicePointResVO;
import com.zans.mms.vo.patrol.PatrolPointRefreshVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface PatrolTaskCheckResultMapper extends Mapper<PatrolTaskCheckResult> {
    List<PatrolTaskCheckResult> getListByCondition(HashMap<String, Object> patrolTaskId);

    int batchInsert(@Param("patrolTaskId") Long patrolTaskId, @Param("pointIds") List<Long> pointIds ,@Param("creator") String creator);

    /**
     * 很具点位获取周围设备，app使用
     * @param params
     * @return
     */
    List<PatrolDevicePointResVO> searchDevicePoint(Map params);

    /**
     * 获取全景数据
     * @return
     */
    List<PatrolAggData> aggTopData(Map params);

    /**
     * 获取单个点位的打卡信息
     * @param pointId
     * @return
     */
    PatrolAppCheckInfo getCheckInfoWithApp(@Param("pointId") Integer pointId);

    /**
     * 打开
     * @param clockIn
     * @return 打卡几个任务
     */
    Integer updateClockIn(PatrolClockIn clockIn);

    /**
     * @Author beiming
     * @Description
     * @Date  4/15/21
     * @Param params
     * @return PatrolAllDevicePointResVO
     **/
    List<PatrolAllDevicePointResVO> searchAllDevicePoint(Map params);

    /**
     * @Author beiming
     * @Description  获取巡检增量更新数据
     * @Date  4/15/21
     * @Param
     * @return
     *@param params */
    List<PatrolPointRefreshVO> searchPatrolRefresh(Map<String, Object> params);

    /**
     * 获取上次巡检结果
     * @param pointId
     * @return
     */
    String getLastCheckResult(Integer pointId);

    Long getCheckResultId(@Param("pointId") Integer pointId);

    void relationTicket(@Param("id") Long resultId,@Param("ticketId") Long ticketId);

    Long getMaxRecord(@Param("dateTime") String dateTime);

    int saveBak(@Param("maxRecordId") Long maxRecordId);

    int deleteHis(@Param("maxRecordId") Long maxRecordId);
}