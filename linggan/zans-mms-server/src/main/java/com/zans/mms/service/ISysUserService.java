package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.base.vo.SelectVO;
import com.zans.base.vo.UserSession;
import com.zans.mms.model.SysUser;
import com.zans.mms.vo.user.SysUserBatchEditReqVO;
import com.zans.mms.vo.user.SysUserResp;
import com.zans.mms.vo.user.SysUserVO;
import com.zans.mms.vo.user.UpdatePasswordReqVO;

import java.util.ArrayList;
import java.util.List;

/**
 * (SysUser)表服务接口
 *
 * @author beixing
 * @since 2021-01-16 16:41:08
 */
public interface ISysUserService {


    List<SelectVO> findUserToSelect(String areaNum,String maintainNum);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysUser queryByIdOrUsername(Integer id, String userName);


    SysUser queryByMobile(String nickName,String mobile);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysUser 实例对象
     * @return 对象列表
     */
    ApiResult list(SysUser sysUser);


    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    int insert(SysUser sysUser);

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    int update(SysUser sysUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    SysUser findUserByName(String userName);

	SysUser getUserByOpenid(String openid);


    ApiResult<SysUserResp> batchEditStatus(SysUserBatchEditReqVO reqVO);

    ApiResult<SysUserResp> updatePassword(Integer id, String password);

    ApiResult batchAddUser(String newFileName, String originName, UserSession userSession);

	List<String> findUsernameListByRoleAndMaintainNum(String roleNum, String allocDepartmentNum);

    List<String> findUsernameListByRoleId(String roleNum);

	List<String> findUsernameListByDepartmentNum(String departmentNum);

	List<String> findUsernameListByRoleList(List<String> roleList);

	List<String> findUsernameListByDeptList(List<String> deptIdList);


	List<String> findUsernameListByDeptListAndRoleList(List<String> deptIdList, List<String> roleIdList);

	List<SysUserVO> getRepairPersonByAreaId(String area);
}
