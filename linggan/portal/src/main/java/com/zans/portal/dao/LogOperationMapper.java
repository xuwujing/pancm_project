package com.zans.portal.dao;

import com.zans.portal.model.LogOperation;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.portal.vo.log.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface LogOperationMapper extends Mapper<LogOperation> {

    List<OpLogRespVO> findLogList(@Param("req") OpLogSearchVO req);

    OpLogRespVO getOpLog(@Param("id") Integer id);

    /**
     * 根据 模块名称，统计各个操作的数量
     *
     * @param module
     * @return
     */
    List<CircleUnit> findGroupOfModule(@Param("module") Integer module);

    List<LogOperation52VO> loginLog(EsLoginLogRespVO esLoginLogRespVO);

    List<LogOperation52VO> operationLog(EsOperationLogRespVO esOperationLogRespVO);

    int updateByTraceId(LogOperation logOperation);

    LogOperation findLogByUserNameRecently(LogOperation logOperation);
}