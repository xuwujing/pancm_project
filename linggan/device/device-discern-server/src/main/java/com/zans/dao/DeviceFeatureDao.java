package com.zans.dao;

import com.zans.model.DeviceFeature;
import com.zans.vo.DeviceFeatureVO;
import com.zans.vo.ResultRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author beixing
 * @Title: (DeviceFeature)表数据库访问层
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-19 14:22:32
 */
@Mapper
public interface DeviceFeatureDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceFeatureVO queryById(Integer id);


    /**
     * 通过实体查询一条数据
     *
     * @param deviceFeatureVO 实例对象
     * @return 对象列表
     */
    DeviceFeatureVO findOne(DeviceFeatureVO deviceFeatureVO);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceFeatureVO 实例对象
     * @return 对象列表
     */
    List<DeviceFeatureVO> queryAll(DeviceFeatureVO deviceFeatureVO);

    /**
     * 新增数据
     *
     * @param deviceFeature 实例对象
     * @return 影响行数
     */
    int insert(DeviceFeature deviceFeature);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeature> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceFeature> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceFeature> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceFeature> entities);

    /**
     * 修改数据
     *
     * @param deviceFeature 实例对象
     * @return 影响行数
     */
    int update(DeviceFeature deviceFeature);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    /**
     * 分析型号
     *
     * @param name 实例对象
     * @return 对象列表
     */
    List<ResultRespVO> analyzeModel(String name,String fieldName);

    /**
     * @Author beixing
     * @Description  分析设备类型
     * @Date  2021/7/14
     * @Param
     * @return
     **/
    List<ResultRespVO> analyzeDeviceType(String name,String fieldName);


    List<ResultRespVO> groupByBusinessId(@Param("businessId") String businessId);

    List<ResultRespVO> groupByBusinessIdAndIp(@Param("businessId") String businessId,@Param("ip") String ip );



    /**
    * @Author beixing
    * @Description  根据设备类型
    * @Date  2021/7/21
    * @Param
    * @return
    **/
    List<ResultRespVO> groupByDeviceType(String fieldName,String fieldNameValue, String model);


    List<ResultRespVO> groupByModel(String fieldName,String fieldNameValue,  String deviceType);







    int reset();

    int resetTable(@Param("entities") List<String> entities);

    int insertDeviceFeatureInfo(@Param("businessId") String businessId);
}

