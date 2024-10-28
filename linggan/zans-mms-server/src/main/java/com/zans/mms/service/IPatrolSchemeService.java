package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.ApiResult;
import com.zans.mms.model.PatrolScheme;
import com.zans.mms.model.PatrolTask;
import com.zans.mms.vo.patrol.PatrolSchemeDetailResVO;
import com.zans.mms.vo.patrol.PatrolSchemeQueryVO;

/**
 * interface PatrolSchemeservice
 *
 * @author
 */
public interface IPatrolSchemeService extends BaseService<PatrolScheme>{

     ApiResult getList(PatrolSchemeQueryVO vo);

     PatrolSchemeDetailResVO getViewById(Long id);

     Boolean existRelation(Long id);

     int deleteByUniqueId(Long id);

     int deleteById(Long id);

    int addSchemeAndJob(PatrolScheme reqVO);

     int updateSchemeAndJob(PatrolScheme reqVO);

     int deleteSchemeAndJob(Long id);

     PatrolTask generateTaskBySchemeId(Long id);
}