package com.zans.dao;

import com.zans.model.DeviceCipher;
import com.zans.vo.DeviceCipherVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (DeviceCipher)表数据库访问层
 *
 * @author beixing
 * @since 2021-08-23 16:15:53
 */
@Mapper
public interface DeviceCipherDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceCipher queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceCipher 实例对象
     * @return 对象列表
     */
    List<DeviceCipher> queryAll(DeviceCipherVO deviceCiphervo);

    /**
     * 新增数据
     *
     * @param deviceCipher 实例对象
     * @return 影响行数
     */
    int insert(DeviceCipher deviceCipher);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceCipher> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceCipher> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceCipher> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceCipher> entities);

    /**
     * 修改数据
     *
     * @param deviceCipher 实例对象
     * @return 影响行数
     */
    int update(DeviceCipher deviceCipher);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    DeviceCipher queryOneByIp(@Param("ip") String ip);

    int updateByIp(DeviceCipher deviceCipher);

}

