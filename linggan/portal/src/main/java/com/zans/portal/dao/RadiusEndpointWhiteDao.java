package com.zans.portal.dao;

import com.zans.portal.model.RadiusEndpointWhite;
import com.zans.portal.vo.RadiusEndpointWhiteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author beixing
 * @Title: 设备白名单表(RadiusEndpointWhite)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022-02-16 18:22:33
 */
@Mapper
public interface RadiusEndpointWhiteDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RadiusEndpointWhiteVO queryById(Long id);


    RadiusEndpointWhiteVO queryByMac(String mac);




    /**
     * 通过实体作为筛选条件查询
     *
     * @param radiusEndpointWhiteVO 实例对象
     * @return 对象列表
     */
    List<RadiusEndpointWhiteVO> queryAll(RadiusEndpointWhiteVO radiusEndpointWhiteVO);

    /**
     * 新增数据
     *
     * @param radiusEndpointWhite 实例对象
     * @return 影响行数
     */
    int insert(RadiusEndpointWhite radiusEndpointWhite);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RadiusEndpointWhite> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RadiusEndpointWhite> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RadiusEndpointWhite> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<RadiusEndpointWhite> entities);

    /**
     * 修改数据
     *
     * @param radiusEndpointWhite 实例对象
     * @return 影响行数
     */
    int update(RadiusEndpointWhite radiusEndpointWhite);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

