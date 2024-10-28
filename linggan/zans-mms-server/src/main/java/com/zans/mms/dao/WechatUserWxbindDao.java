package com.zans.mms.dao;

import com.zans.mms.model.WechatUserWxbind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (WechatUserWxbind)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-07 17:00:34
 */
@Mapper
public interface WechatUserWxbindDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WechatUserWxbind queryById(Integer id);

    WechatUserWxbind queryByOpenId(String appId, String openId);


    List<String> queryByOrgId(String orgId,String roleId,int weChatEnable,int weChatPushEnable);



    /**
     * 通过实体作为筛选条件查询
     *
     * @param wechatUserWxbind 实例对象
     * @return 对象列表
     */
    List<WechatUserWxbind> queryAll(WechatUserWxbind wechatUserWxbind);

    /**
     * 新增数据
     *
     * @param wechatUserWxbind 实例对象
     * @return 影响行数
     */
    int insert(WechatUserWxbind wechatUserWxbind);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<WechatUserWxbind> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<WechatUserWxbind> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<WechatUserWxbind> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<WechatUserWxbind> entities);

    /**
     * 修改数据
     *
     * @param wechatUserWxbind 实例对象
     * @return 影响行数
     */
    int update(WechatUserWxbind wechatUserWxbind);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    int deleteByUserName(String userName);

}

