package com.zans.mms.service.impl;


import com.zans.base.exception.RollbackException;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.mms.BaseOrgRoleMapper;
import com.zans.mms.model.BaseOrgRole;
import com.zans.mms.model.SysRolePerm;
import com.zans.mms.service.IBaseOrgRoleService;
import com.zans.mms.service.IConstantItemService;
import com.zans.mms.service.IPermissionService;
import com.zans.mms.vo.perm.PermissionRespVO;
import com.zans.mms.vo.role.RoleEditVO;
import com.zans.mms.vo.role.RoleRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 *  BaseOrgRoleServiceImpl
 *
 *  @author
 */
@Slf4j
@Service("baseOrgRoleService")
public class BaseOrgRoleServiceImpl extends BaseServiceImpl<BaseOrgRole> implements IBaseOrgRoleService {

    @Autowired
    IPermissionService permissionService;

	@Autowired
	private BaseOrgRoleMapper baseOrgRoleMapper;

    @Autowired
    IConstantItemService constantItemService;

    @Resource
    public void setBaseOrgRoleMapper(BaseOrgRoleMapper baseMapper) {
        super.setBaseMapper(baseMapper);
        this.baseOrgRoleMapper = baseMapper;
    }

    @Override
    public List<SelectVO> orgRoleList() {
        return baseOrgRoleMapper.orgRoleList();
    }

    @Override
    public List<SelectVO> getRoleBMaintain(String maintainNum) {
        return baseOrgRoleMapper.getRoleBMaintain(maintainNum);
    }

    @Override
    public RoleRespVO findRoleById(String roleId, Integer moduleType) {
        RoleRespVO role = baseOrgRoleMapper.findRoleById(roleId);

        List<PermissionRespVO> permList = permissionService.findPermissionByRole(roleId,moduleType);
        List<Integer> checkedPermissions = new LinkedList<>();
        for (PermissionRespVO vo : permList) {
            // el-tree 只要 叶子节点
            checkedPermissions.add(vo.getPermId());
            vo.resetProperties();
        }
//        role.setPermList(permList);
        role.setCheckedPermission(checkedPermissions);
        List<SysRolePerm> checkedDataPerm = permissionService.findDataPermList(roleId);
        role.setCheckedDataPerm(checkedDataPerm);
        return role;

    }

    @Override
    public List<RoleRespVO> findAllRoles(String roleName) {
        List<RoleRespVO> list = baseOrgRoleMapper.findAllRoles(roleName);
        return list;
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public int editRole(RoleEditVO vo) {
        permissionService.updateRolePermission(vo);
        return 1;
    }
}
