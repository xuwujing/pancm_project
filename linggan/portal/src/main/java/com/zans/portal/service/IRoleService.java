package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.SelectVO;
import com.zans.portal.model.SysRole;
import com.zans.portal.vo.role.RoleAddVO;
import com.zans.portal.vo.role.RoleAndUserRespVO;
import com.zans.portal.vo.role.RoleEditVO;
import com.zans.portal.vo.role.RoleRespVO;

import java.util.List;

public interface IRoleService extends BaseService<SysRole> {


    List<SelectVO> findRoleToSelect();

    SysRole findRoleByName(String roleName);

    SysRole findRoleByNameExceptId(String roleName, Integer roleId);

    /**
     * 根据 enable 查询角色
     *
     * @param enable 禁用状态；null 查询所有角色
     * @return 角色列表
     */
    List<RoleRespVO> findAllRoles(String role_name,Integer enable);

    RoleRespVO findRoleById(Integer roleId);

    List<RoleAndUserRespVO> findRoleAndUserById(Integer roleId);

    int editRole(RoleEditVO vo);

    int saveRole(RoleAddVO vo);
}
