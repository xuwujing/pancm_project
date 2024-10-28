package com.zans.mms.dao.guard;

import com.zans.mms.model.SysConstant;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface SysConstantMapper extends Mapper<SysConstant> {

	SysConstant findConstantByKey(@Param("key") String key);

}