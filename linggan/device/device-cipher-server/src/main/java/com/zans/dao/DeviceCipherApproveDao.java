package com.zans.dao;

import com.zans.model.DeviceCipherApprove;
import com.zans.vo.DeviceCipherApproveVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (DeviceCipherApprove)表数据库访问层
 *
 * @author beixing
 * @since 2021-08-23 16:23:48
 */
@Mapper
public interface DeviceCipherApproveDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceCipherApprove queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceCipherApprove 实例对象
     * @return 对象列表
     */
    List<DeviceCipherApproveVO> queryAll(DeviceCipherApproveVO deviceCipherApproveVO);

    /**
     * 新增数据
     *
     * @param deviceCipherApprove 实例对象
     * @return 影响行数
     */
    int insert(DeviceCipherApprove deviceCipherApprove);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceCipherApprove> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceCipherApprove> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceCipherApprove> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceCipherApprove> entities);

    /**
     * 修改数据
     *
     * @param deviceCipherApprove 实例对象
     * @return 影响行数
     */
    int update(DeviceCipherApprove deviceCipherApprove);

    int updateStatus(DeviceCipherApproveVO deviceCipherApprove);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    DeviceCipherApprove queryOne(DeviceCipherApprove deviceCipherApprove);

    List<DeviceCipherApproveVO> queryApproveList(DeviceCipherApproveVO deviceCipherApproveVO);

//    int updateStatus(List<DeviceCipherApproveVO> list);

}

