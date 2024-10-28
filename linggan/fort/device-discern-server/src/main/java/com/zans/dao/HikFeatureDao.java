package com.zans.dao;

import com.zans.model.HikFeature;
import com.zans.vo.HikFeatureVO;
import com.zans.vo.ResultRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author beixing
 * @Title: (HikFeature)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-14 11:04:01
 */
@Mapper
public interface HikFeatureDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    HikFeatureVO queryById(Integer id);


    /**
     * 通过实体查询一条数据
     *
     * @param hikFeatureVO 实例对象
     * @return 对象列表
     */
    HikFeatureVO findOne(HikFeatureVO hikFeatureVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param hikFeatureVO 实例对象
     * @return 对象列表
     */
    List<HikFeatureVO> queryAll(HikFeatureVO hikFeatureVO);



    /**
     * 分析型号
     *
     * @param count 实例对象
     * @return 对象列表
     */
    List<ResultRespVO> analyzeModel( long count);

    /**
    * @Author beixing
    * @Description  分析设备类型
    * @Date  2021/7/14
    * @Param
    * @return
    **/
    List<ResultRespVO> analyzeDeviceType(long count);


    List<ResultRespVO> groupByVersion();



    /**
     * 新增数据
     *
     * @param hikFeature 实例对象
     * @return 影响行数
     */
    int insert(HikFeature hikFeature);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<HikFeature> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<HikFeature> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<HikFeature> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<HikFeature> entities);

    /**
     * 修改数据
     *
     * @param hikFeature 实例对象
     * @return 影响行数
     */
    int update(HikFeature hikFeature);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

