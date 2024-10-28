package com.zans.dao;


import com.zans.model.SysUser;
import com.zans.vo.SelectVO;
import com.zans.vo.SysUserResp;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysUserMapper extends Mapper<SysUser> {

    List<SelectVO> querySysUser(@Param("areaNum") String areaNum, @Param("maintainNum") String maintainNum);



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

    /**
     * @Author beiming
     * @Description  通过openId获取用户信息
     * @Date  4/25/21
     * @Param
     * @return
     **/

    SysUser getUserByOpenid(String openid);

    void updateProjectId(@Param("userId") Integer userId, @Param("projectId")String projectId);

    SysUser getById(Integer userId);

    SysUser findCurrentUser(@Param("ip") String ip);

}