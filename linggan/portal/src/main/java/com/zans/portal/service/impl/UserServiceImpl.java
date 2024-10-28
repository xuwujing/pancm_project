package com.zans.portal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.service.impl.BaseServiceImpl;
import com.zans.base.vo.PageResult;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.dao.SysUserRoleMapper;
import com.zans.portal.dao.TUserMapper;
import com.zans.portal.model.SysUserRole;
import com.zans.portal.model.TUser;
import com.zans.portal.service.IConstantItemService;
import com.zans.portal.service.IUserService;
import com.zans.portal.vo.role.RoleAndUserRespVO;
import com.zans.portal.vo.user.UserReqVO;
import com.zans.portal.vo.user.UserRespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService {

    TUserMapper userMapper;

    @Autowired
    SysUserRoleMapper userRoleMapper;

    @Autowired
    IConstantItemService constantItemService;



    @Resource
    public void setUserMapper(TUserMapper userMapper) {
        super.setBaseMapper(userMapper);
        this.userMapper = userMapper;
    }

    @Override
    public TUser findUserByName(String userName) {
        return userMapper.findUserByName(userName);
    }

    @Override
    public TUser findUserByNameExceptId(String userName, Integer userId) {
        return userMapper.findUserByNameExceptId(userName, userId);
    }

    @Override
    public int changeUserEnableStatus(Integer userId, int status) {
        return userMapper.changeUserEnableStatus(userId, status);
    }

    @Override
    public int addUserRole(Integer userId, Integer roleId) {
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        return userRoleMapper.insert(userRole);
    }

    @Override
    public int updateUserRole(Integer userId, Integer roleId) {
        return userRoleMapper.updateRoleOfUser(userId, roleId);
    }

    @Override
    public List<UserRespVO> findUserList() {
        Map<Object, String> enableMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_ENABLE_STATUS);
        Map<Object, String> lockMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_USER_LOCK);
        Map<Object, String> departMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_USER_DEPARTMENT);
        List<UserRespVO> list = userMapper.findUserList();
        for (UserRespVO vo : list) {
            vo.resetEnableName(enableMap);
            vo.resetLockStatusName(lockMap);
            vo.resetDepartName(departMap);
        }
        return list;
    }

    @Override
    public PageResult<UserRespVO> getUserPageWHJG(UserReqVO reqVO) {

        Page page = PageHelper.startPage(reqVO.getPageNum(), reqVO.getPageSize());

        List<UserRespVO> list = userMapper.findUserPageWHJG(reqVO);

        return new PageResult<UserRespVO>(page.getTotal(), page.getResult(), reqVO.getPageSize(), reqVO.getPageNum());
    }



    @Override
    public UserRespVO findUserByIdWHJG(Integer id) {
        UserRespVO vo = userMapper.findUserByIdWHJG(id);
        if (vo != null) {
            Map<Object, String> enableMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_ENABLE_STATUS);
            Map<Object, String> lockMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_USER_LOCK);
            Map<Object, String> departMap = constantItemService.findItemsMapByDict(GlobalConstants.MODULE_USER_DEPARTMENT);
            vo.resetEnableName(enableMap);
            vo.resetLockStatusName(lockMap);
            vo.resetDepartName(departMap);
        }
        return vo;
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUserById(Integer id) {
        userMapper.deleteById(id);

        userRoleMapper.deleteByUserId(id);
    }

    @Override
    public RoleAndUserRespVO findUserRoleById(Integer id) {
       return userRoleMapper.findUserRoleById(id);
    }
}
