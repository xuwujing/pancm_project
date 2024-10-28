package com.zans.mms.dao.guard;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.zans.mms.model.OpsJobExecution;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
@DS("job")
public interface OpsJobExecutionMapper extends Mapper<OpsJobExecution> {

    Integer count();

}