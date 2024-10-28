package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.portal.model.LogOperation;
import com.zans.portal.vo.chart.CircleUnit;
import com.zans.base.vo.PageResult;
import com.zans.portal.vo.log.*;
import com.zans.portal.vo.user.TUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ILogOperationService extends BaseService<LogOperation> {

    PageResult<OpLogRespVO> getPage(OpLogSearchVO req);

    OpLogRespVO getOpLog(Integer id);

    /**
     * 根据 模块名称，统计各个操作的数量
     * @param module
     * @return
     */
    List<CircleUnit> findGroupOfModule(@Param("module") Integer module);

    ApiResult loginLog(EsLoginLogRespVO esLoginLogRespVO);

    ApiResult operationLog(EsOperationLogRespVO esOperationLogRespVO);

    ApiResult init();

    ApiResult onLineList(TUserVO tUserVO);

    ApiResult heartbeat(Integer userId);

    void statisticalOffline();

    int updateByTraceId(LogOperation logOperation);
}
