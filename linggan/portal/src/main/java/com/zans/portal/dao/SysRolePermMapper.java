package com.zans.portal.dao;

import com.zans.portal.model.SysRolePerm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface SysRolePermMapper extends Mapper<SysRolePerm> {

}