package com.zans.job.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.util.DateHelper;
import com.zans.base.vo.PageResult;
import com.zans.job.config.JobHourCountCache;
import com.zans.job.dao.OpsJobMapper;
import com.zans.job.model.OpsJob;
import com.zans.job.service.IJobService;
import com.zans.job.vo.common.CircleUnit;
import com.zans.job.vo.job.JobReqVO;
import com.zans.job.vo.job.JobRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author xv
 * @since 2020/5/7 17:22
 */
@Slf4j
@Service("jobService")
public class JobServiceImpl extends BaseServiceImpl<OpsJob> implements IJobService {

    OpsJobMapper jobMapper;

    @Autowired
    private    JobHourCountCache jobHourCountCache;

    @Resource
    public void setJobMapper(OpsJobMapper jobMapper) {
        super.setBaseMapper(jobMapper);
        this.jobMapper = jobMapper;
    }


    @Override
    public PageResult<JobRespVO> getJobPage(JobReqVO reqVO) {
        String orderBy = reqVO.getSortOrder();
        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<JobRespVO> list = jobMapper.findJobList(reqVO);
        return new PageResult<JobRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }

    @Override
    public int getJobByJobNameAndId(String jobName, Integer jobId) {
        return jobMapper.getJobByJobNameAndId(jobName, jobId);
    }

    @Override
    public List<CircleUnit> getJobCount() {
        return jobMapper.getJobCount();
    }

    @Override
    public List<CircleUnit> getJobPieChart() {
        return jobMapper.getJobPieChart();
    }

    @Override
    public List<CircleUnit> executes() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String beforeDay = DateHelper.formatDate(cal.getTime(), "yyyy-MM-dd");
        List<String> dates = new ArrayList<>();
        List<CircleUnit> result = new ArrayList<>();
        List<CircleUnit> unitList = new ArrayList<>();
        String[] hours = new String[24];
        String hour = "";
        for (int i = 0; i < 24; i++) {
            hour = i < 10 ? " 0" + i : " " + i;
            String date = beforeDay + hour;
            hours[i] = String.valueOf(i);
            CircleUnit circleUnit = jobHourCountCache.getJobHour(date);
            if(circleUnit == null){
                dates.add(date);
            }else {
                unitList.add(circleUnit);
            }
        }
        if(dates.size()>0){
            result = jobMapper.getJobByHours(dates);
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setName(hours[i]);
                jobHourCountCache.putJobHour(dates.get(i),result.get(i));
            }
        }
        if(unitList.size()>0){
            result.addAll(unitList);
        }
        return result;
    }

}
