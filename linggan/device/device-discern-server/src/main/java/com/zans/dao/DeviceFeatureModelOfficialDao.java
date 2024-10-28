package com.zans.dao;

import com.zans.model.DeviceFeatureModelOfficial;
import com.zans.vo.DeviceFeatureModelOfficialVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author beixing
 * @Title: 设备型号和类型对应表(DeviceFeatureModelOfficial)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 18:14:05
 */
@Mapper
public interface DeviceFeatureModelOfficialDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceFeatureModelOfficialVO queryById(Object id);


    /**
     * 通过实体查询一条数据
     *
     * @param deviceFeatureModelOfficialVO 实例对象
     * @return 对象列表
     */
    DeviceFeatureModelOfficialVO findOne(DeviceFeatureModelOfficialVO deviceFeatureModelOfficialVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceFeatureModelOfficialVO 实例对象
     * @return 对象列表
     */
    List<DeviceFeatureModelOfficialVO> queryAll(DeviceFeatureModelOfficialVO deviceFeatureModelOfficialVO);

    /**
     * 新增数据
     *
     * @param deviceFeatureModelOfficial 实例对象
     * @return 影响行数
     */
    int insert(DeviceFeatureModelOfficial deviceFeatureModelOfficial);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureModelOfficial> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceFeatureModelOfficial> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeatureModelOfficial> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceFeatureModelOfficial> entities);

    /**
     * 修改数据
     *
     * @param deviceFeatureModelOfficial 实例对象
     * @return 影响行数
     */
    int update(DeviceFeatureModelOfficial deviceFeatureModelOfficial);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Object id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param
     * @return 对象列表
     */
    List<DeviceFeatureModelOfficialVO> queryByModel();

    /**
    * @Author beixing
    * @Description  球机分析
    * @Date  2021/7/29
    * @Param
    * @return
    **/
    List<DeviceFeatureModelOfficialVO> queryByBall();


    /**
     * @Author beixing
     * @Description  网络摄像机分析
     * @Date  2021/7/29
     * @Param
     * @return
     **/
    List<DeviceFeatureModelOfficialVO> queryByCamera();


}

