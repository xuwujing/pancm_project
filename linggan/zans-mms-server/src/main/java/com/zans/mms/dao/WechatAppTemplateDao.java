package com.zans.mms.dao;

import com.zans.mms.model.WechatAppTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信app模板配置表(WechatAppTemplate)表数据库访问层
 *
 * @author beixing
 * @since 2021-03-11 16:49:56
 */
@Mapper
public interface WechatAppTemplateDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WechatAppTemplate queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param wechatAppTemplate 实例对象
     * @return 对象列表
     */
    List<WechatAppTemplate> queryAll(WechatAppTemplate wechatAppTemplate);

    WechatAppTemplate findOne(WechatAppTemplate wechatAppTemplate);

    /**
     * 新增数据
     *
     * @param wechatAppTemplate 实例对象
     * @return 影响行数
     */
    int insert(WechatAppTemplate wechatAppTemplate);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<WechatAppTemplate> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<WechatAppTemplate> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<WechatAppTemplate> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<WechatAppTemplate> entities);

    /**
     * 修改数据
     *
     * @param wechatAppTemplate 实例对象
     * @return 影响行数
     */
    int update(WechatAppTemplate wechatAppTemplate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

