package com.zans.mms.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.exception.BusinessException;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.mms.DevicePointSubsetDetailMapper;
import com.zans.mms.dao.mms.PatrolSchemeMapper;
import com.zans.mms.dao.mms.PatrolTaskCheckResultMapper;
import com.zans.mms.dao.mms.PatrolTaskMapper;
import com.zans.mms.model.CronJob;
import com.zans.mms.model.PatrolScheme;
import com.zans.mms.model.PatrolTask;
import com.zans.mms.service.ICronJobService;
import com.zans.mms.service.IPatrolSchemeService;
import com.zans.mms.service.IPatrolTaskService;
import com.zans.mms.vo.patrol.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *  PatrolSchemeServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("patrolSchemeService")
public class PatrolSchemeServiceImpl extends BaseServiceImpl<PatrolScheme> implements IPatrolSchemeService {


	@Autowired
	private PatrolSchemeMapper patrolSchemeMapper;
	@Autowired
	private DevicePointSubsetDetailMapper devicePointSubsetDetailMapper;
	@Autowired
	private PatrolTaskMapper patrolTaskMapper;

	@Autowired
	private IPatrolTaskService patrolTaskService;

	@Autowired
	ICronJobService cronJobService;

	@Autowired
	private PatrolTaskCheckResultMapper patrolTaskCheckResultMapper;

    @Resource
    public void setPatrolSchemeMapper(PatrolSchemeMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.patrolSchemeMapper = baseMapper;
    }

    @Override
    public ApiResult getList(PatrolSchemeQueryVO vo) {
        int pageNum = vo.getPageNum();
        int pageSize = vo.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);

        List<PatrolSchemeResVO> result = patrolSchemeMapper.getList(vo);
		for (PatrolSchemeResVO schemeResVO : result) {
		    int count =	devicePointSubsetDetailMapper.getCountBySubsetId(schemeResVO.getSubsetId());
		    schemeResVO.setPointCount(count);
		}

        return ApiResult.success(new PageResult<PatrolSchemeResVO>(page.getTotal(), result, pageSize, pageNum));
     }

	@Override
	public PatrolSchemeDetailResVO getViewById(Long id) {
		PatrolSchemeDetailResVO resVo = patrolSchemeMapper.getViewById(id);
		return resVo;
	}

	@Override
	public Boolean existRelation(Long id) {
		PatrolTaskQueryVO vo = new PatrolTaskQueryVO();
		vo.setPatrolTaskId(id);
		List<PatrolTaskResVO> list = patrolTaskMapper.getList(vo);
		return !list.isEmpty() && list.size()>0;

	}

	@Override
	public int deleteByUniqueId(Long id) {
		return patrolSchemeMapper.deleteByUniqueId(id);
	}


	@Override
	public int deleteById(Long id) {
		return patrolSchemeMapper.deleteById(id);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public int addSchemeAndJob(PatrolScheme reqVO) {
    	this.saveSelective(reqVO);
		Date nextTime = DateHelper.plusDays(new Date(), reqVO.getPatrolPeriod());

		CronJob cronJob = new CronJob();
		cronJob.setJobName(reqVO.getSchemeName());
		cronJob.setJobType("patrol");
		cronJob.setRelationId(reqVO.getId());
		cronJob.setPrevTime(new Date());
		cronJob.setNextTime(nextTime);
		cronJob.setEnableStatus("启用");
		cronJob.setCreator(reqVO.getCreator());

		return cronJobService.saveSelective(cronJob);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateSchemeAndJob(PatrolScheme reqVO) {
    	this.updateSelective(reqVO);

    	int count = cronJobService.updateNextTime("patrol",reqVO.getId(),reqVO.getPatrolPeriod());
		return count;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteSchemeAndJob(Long id) {
    	this.deleteById(id);
		return cronJobService.deleteByTypeAndId("patrol",id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public PatrolTask generateTaskBySchemeId(Long id) {
		PatrolScheme patrolScheme = this.getById(id);

		List<Long> pointIds = devicePointSubsetDetailMapper.getPointIdsBySubsetId(patrolScheme.getSubsetId());
		if (pointIds == null ||pointIds.size()==0){
			throw new BusinessException("pointIds  is null");
		}

		String nowDateStr = DateHelper.getNow();
		Date nowDate = new Date();
		PatrolTask lastTask = patrolTaskMapper.getLastRecord(id);
		if (lastTask != null){
			String endTime = DateHelper.formatDate(lastTask.getEndTime(),"yyyy-MM-dd")+ " 23:59:59";
			if (nowDateStr.compareTo(endTime)<0) {
				throw new BusinessException("上一次巡检任务未完成，taskName: "+lastTask.getTaskName());
			}
		}

		Date nextTime = DateHelper.plusDays(nowDate, patrolScheme.getPatrolPeriod()-1);
		String dateTime = DateHelper.formatDate(new Date(),"yyyyMMdd");


		PatrolTask patrolTask = new PatrolTask();
		patrolTask.setPatrolSchemeId(id);
		patrolTask.setOrgId(patrolScheme.getOrgId());
		patrolTask.setTaskName(patrolScheme.getSchemeName()+"-"+dateTime);
		patrolTask.setDescription(patrolScheme.getDescription());
		patrolTask.setStartTime(nowDate);
		patrolTask.setEndTime(nextTime);
		patrolTask.setPointCount(pointIds.size());
		patrolTask.setFinishedCount(0);
		patrolTask.setCreator(patrolScheme.getCreator());
		patrolTaskService.saveSelective(patrolTask);

		int count = patrolTaskCheckResultMapper.batchInsert(patrolTask.getId(),pointIds,patrolScheme.getCreator());

		return patrolTask;
	}
}
