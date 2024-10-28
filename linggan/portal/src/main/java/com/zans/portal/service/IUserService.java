package com.zans.portal.service;

import com.zans.base.service.BaseService;
import com.zans.base.vo.PageResult;
import com.zans.portal.model.TUser;
import com.zans.portal.vo.role.RoleAndUserRespVO;
import com.zans.portal.vo.user.UserReqVO;
import com.zans.portal.vo.user.UserRespVO;

import java.util.List;

public interface IUserService extends BaseService<TUser> {

    TUser findUserByName(String userName);

    TUser findUserByNameExceptId(String userName, Integer userId);

    int changeUserEnableStatus(Integer userId, int status);

    int addUserRole(Integer userId, Integer roleId);

    int updateUserRole(Integer userId, Integer roleId);

    List<UserRespVO> findUserList();

    UserRespVO findUserByIdWHJG(Integer id);

    PageResult<UserRespVO> getUserPageWHJG(UserReqVO reqVO);



    void deleteUserById(Integer id);

    RoleAndUserRespVO findUserRoleById(Integer id);
}
