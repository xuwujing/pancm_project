package com.zans.portal.dao;

import com.zans.portal.model.SysUserRole;
import com.zans.portal.vo.role.RoleAndUserRespVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysUserRoleMapper extends Mapper<SysUserRole> {

    int updateRoleOfUser(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    List<RoleAndUserRespVO> findRoleUser(@Param("roleId") Integer roleId);

    int deleteByUserId(@Param("userId") Integer userId);

    RoleAndUserRespVO findUserRoleById(Integer id);
}
