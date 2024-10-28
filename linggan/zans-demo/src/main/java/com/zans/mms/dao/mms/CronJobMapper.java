package com.zans.mms.dao.mms;

import com.zans.mms.model.CronJob;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

@Repository
public interface CronJobMapper extends Mapper<CronJob> {
    int deleteByTypeAndId(@Param("jobType") String jobType, @Param("relationId") Long id);

    int updateNextTime(@Param("jobType")String jobType,@Param("relationId")  Long id, @Param("period") Integer period);

    List<CronJob> getListByCondition(HashMap<String, Object> map);
}
