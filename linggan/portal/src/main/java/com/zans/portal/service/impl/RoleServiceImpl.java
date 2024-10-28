package com.zans.portal.service.impl;

import com.zans.base.exception.RollbackException;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.SelectVO;
import com.zans.portal.dao.SysRoleMapper;
import com.zans.portal.dao.SysUserRoleMapper;
import com.zans.portal.model.SysRole;
import com.zans.portal.service.IConstantItemService;
import com.zans.portal.service.IPermissionService;
import com.zans.portal.service.IRoleService;
import com.zans.portal.vo.perm.PermissionRespVO;
import com.zans.portal.vo.role.RoleAddVO;
import com.zans.portal.vo.role.RoleAndUserRespVO;
import com.zans.portal.vo.role.RoleEditVO;
import com.zans.portal.vo.role.RoleRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.zans.portal.config.GlobalConstants.MODULE_ENABLE_STATUS;

@Service
@Slf4j
public class RoleServiceImpl extends BaseServiceImpl<SysRole> implements IRoleService {

    @Autowired
    IPermissionService permissionService;

    SysRoleMapper roleMapper;

    @Autowired
    SysUserRoleMapper userRoleMapper;

    @Autowired
    IConstantItemService constantItemService;

    @Resource
    public void setRoleMapper(SysRoleMapper roleMapper) {
        super.setBaseMapper(roleMapper);
        this.roleMapper = roleMapper;
    }

    @Override
    public List<SelectVO> findRoleToSelect() {
        return roleMapper.findRoleToSelect();
    }

    @Override
    public List<RoleRespVO> findAllRoles(String roleName,Integer enable) {
        Map<Object, String> enableMap = constantItemService.findItemsMapByDict(MODULE_ENABLE_STATUS);
        List<RoleRespVO> list = roleMapper.findAllRoles(roleName,enable);
        for (RoleRespVO vo : list) {
            vo.resetEnableNameByMap(enableMap);
        }
        return list;
    }

    @Override
    public RoleRespVO findRoleById(Integer roleId) {
        RoleRespVO role = roleMapper.findRoleById(roleId);

        Map<Object, String> enableMap = constantItemService.findItemsMapByDict(MODULE_ENABLE_STATUS);
        role.resetEnableNameByMap(enableMap);

        List<PermissionRespVO> permList = permissionService.findPermissionByRole(roleId);
        List<Integer> checkedPermissions = new LinkedList<>();
        for (PermissionRespVO vo : permList) {
            // el-tree 只要 叶子节点
            checkedPermissions.add(vo.getPermId());
            vo.resetProperties();
        }
        role.setPermList(permList);
        role.setCheckedPermission(checkedPermissions);
        return role;
    }

    @Override
    public List<RoleAndUserRespVO> findRoleAndUserById(Integer roleId) {
        return userRoleMapper.findRoleUser(roleId);
    }


    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public int editRole(RoleEditVO vo) {
        SysRole role = this.getById(vo.getRoleId());
        if (role == null) {
            return -1;
        }
        BeanUtils.copyProperties(vo, role);
        this.update(role);
        permissionService.updateRolePermission(role.getRoleId(), vo.getPermIds());
        return role.getRoleId();
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    public int saveRole(RoleAddVO vo) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(vo, role);
        if(role.getEnable() == null){
            role.setEnable(1);
        }
        int i = this.save(role);
        log.info("saveRole, {}, {}", role.getRoleId(), i);
        permissionService.updateRolePermission(role.getRoleId(), vo.getPermIds());
        return role.getRoleId();
    }

    @Override
    public SysRole findRoleByName(String roleName) {
        return roleMapper.findRoleByName(roleName);
    }

    @Override
    public SysRole findRoleByNameExceptId(String roleName, Integer roleId) {
        return roleMapper.findRoleByNameExceptId(roleName, roleId);
    }
}
