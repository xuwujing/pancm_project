package com.zans.mms.dao.mms;

import com.zans.mms.model.WechatConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信配置文件表(WechatConfig)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-11 11:45:06
 */
@Mapper
public interface WechatConfigDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WechatConfig queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param wechatConfig 实例对象
     * @return 对象列表
     */
    List<WechatConfig> queryAll(WechatConfig wechatConfig);

    /**
     * 新增数据
     *
     * @param wechatConfig 实例对象
     * @return 影响行数
     */
    int insert(WechatConfig wechatConfig);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<WechatConfig> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<WechatConfig> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<WechatConfig> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<WechatConfig> entities);

    /**
     * 修改数据
     *
     * @param wechatConfig 实例对象
     * @return 影响行数
     */
    int update(WechatConfig wechatConfig);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

