package com.zans.dao;

import com.zans.model.SysConstant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysConstantDao extends tk.mybatis.mapper.common.Mapper<SysConstant> {

    List<SysConstant> queryPwd(SysConstant sysConstant);

}
