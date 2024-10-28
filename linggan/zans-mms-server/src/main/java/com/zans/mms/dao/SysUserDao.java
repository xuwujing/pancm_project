package com.zans.mms.dao;

import com.zans.base.vo.SelectVO;
import com.zans.mms.model.SysUser;
import com.zans.mms.vo.user.SysUserResp;
import com.zans.mms.vo.user.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * (SysUser)表数据库访问层
 *
 * @author beixing
 * @since 2021-01-16 16:41:08
 */
@Mapper
public interface SysUserDao {

    List<SelectVO> querySysUser(@Param("areaNum") String areaNum,@Param("maintainNum") String maintainNum);



    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysUser queryByIdOrUsername(@Param("id") Integer id, @Param("userName") String userName);

    SysUser queryByMobile(@Param("nickName") String nickName,@Param("mobile") String mobile);



    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysUser 实例对象
     * @return 对象列表
     */
    List<SysUserResp> queryAll(SysUser sysUser);

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 影响行数
     */
    int insert(SysUser sysUser);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysUser> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysUser> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysUser> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<SysUser> entities);

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 影响行数
     */
    int update(SysUser sysUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    SysUser findUserByName(String userName);

	List<String> findUsernameListByRoleAndMaintainNum(String roleNum, String allocDepartmentNum);

	List<String> findUsernameListByRoleList(@Param("roleList") List<String> roleList);

	List<String> findUsernameListByDeptList(List<String> deptIdList);

	List<String> findUsernameListByDeptListAndRoleList(@Param("deptIdList") List<String> deptIdList,@Param("roleIdList") List<String> roleIdList);

	SysUser getUserByOpenid(String openid);

	List<SysUserVO> getRepairPersonByAreaId(@Param("area") String area);

	String getNames(@Param("usernameList") List<String> repairPersonList);

	String getPhone(@Param("nickname") String nickname);

	List<String> getByOperator(@Param("operator") String operator);
}
