package com.zans.portal.dao;

import com.zans.portal.model.LogOperation;
import com.zans.portal.model.SysRole;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.TUser;
import com.zans.portal.vo.role.RoleRespVO;
import com.zans.portal.vo.user.TUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysRoleMapper extends Mapper<SysRole> {

    List<SelectVO> findRoleToSelect();

    List<RoleRespVO> findAllRoles(@Param("roleName")String roleName,@Param("enable") Integer enable);

    RoleRespVO findRoleById(@Param("roleId") Integer roleId);

    SysRole findRoleByName(@Param("roleName") String roleName);

    SysRole findRoleByNameExceptId(@Param("roleName") String roleName, @Param("roleId") Integer roleId);

    SysRole selectRoleName(TUserVO tUser);
}