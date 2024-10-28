package com.zans.portal.dao;

import com.zans.portal.model.AssessRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审核申述表(AssessRecord)表数据库访问层
 *
 * @author beixing
 * @since 2021-11-03 10:07:48
 */
@Mapper
public interface AssessRecordDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AssessRecord queryById(Integer id);







    /**
     * 通过实体作为筛选条件查询
     *
     * @param assessRecord 实例对象
     * @return 对象列表
     */
    List<AssessRecord> queryAll(AssessRecord assessRecord);

    /**
     * 新增数据
     *
     * @param assessRecord 实例对象
     * @return 影响行数
     */
    int insert(AssessRecord assessRecord);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssessRecord> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AssessRecord> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AssessRecord> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AssessRecord> entities);

    /**
     * 修改数据
     *
     * @param assessRecord 实例对象
     * @return 影响行数
     */
    int update(AssessRecord assessRecord);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    AssessRecord findByNetworkScanIdAndIp(@Param("networkScanId") Integer networkScanId, @Param("swIp") String swIp);
}

