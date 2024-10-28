package com.zans.mms.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.SelectVO;
import com.zans.mms.model.BaseOrgRole;
import com.zans.mms.vo.role.RoleEditVO;
import com.zans.mms.vo.role.RoleRespVO;

import java.util.List;

/**
 * interface BaseOrgRoleservice
 *
 * @author
 */
public interface IBaseOrgRoleService extends BaseService<BaseOrgRole>{


     List<SelectVO> orgRoleList();

     List<SelectVO> getRoleBMaintain(String maintainNum);

    RoleRespVO findRoleById(String id, Integer moduleType);

    List<RoleRespVO> findAllRoles(String roleName);

    int editRole(RoleEditVO req);

	String findRoleByName(String roleName);

	List<String> getIdByName(List<String> roleNameList);
}
