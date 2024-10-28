package com.zans.dao;

import com.zans.model.SysConstant;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysConstantMapper extends Mapper<SysConstant> {

    List<SysConstant> getDecodeInfo();

}