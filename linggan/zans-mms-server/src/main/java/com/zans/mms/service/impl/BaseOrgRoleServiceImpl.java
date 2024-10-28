package com.zans.mms.service.impl;


import com.zans.base.exception.RollbackException;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.SelectVO;
import com.zans.mms.dao.BaseOrgRoleMapper;
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
    public RoleRespVO findRoleById(String jurisdictionId, Integer moduleType) {
        RoleRespVO role = baseOrgRoleMapper.findJurisdictionById(jurisdictionId);

        List<PermissionRespVO> permList = permissionService.findPermissionByRole(jurisdictionId,moduleType);
        List<Integer> checkedPermissions = new LinkedList<>();
        for (PermissionRespVO vo : permList) {
            // el-tree 只要 叶子节点
            checkedPermissions.add(vo.getPermId());
            vo.resetProperties();
        }
//        role.setPermList(permList);
        role.setCheckedPermission(checkedPermissions);
        List<SysRolePerm> checkedDataPerm = permissionService.findDataPermList(jurisdictionId);
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

    /**
     * 通过角色名称查询角色id
     * @param roleName 角色名称
     * @return
     */
    @Override
    public String findRoleByName(String roleName) {
        return baseOrgRoleMapper.findRoleByName(roleName);
    }

    /**
     * 通过名字获取id
     * @param roleIdList
     * @return
     */
    @Override
    public List<String> getIdByName(List<String> roleIdList) {
        return baseOrgRoleMapper.getIdByName(roleIdList);
    }
}
