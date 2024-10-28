package com.zans.service;

import com.zans.model.DeviceCipherHis;
import com.zans.vo.ApiResult;

import java.util.List;

/**
 * (DeviceCipherHis)表服务接口
 *
 * @author beixing
 * @since 2021-08-23 16:15:55
 */
public interface IDeviceCipherHisService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceCipherHis queryById(Integer id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param deviceCipherHis 实例对象
     * @return 对象列表
     */
    ApiResult list(DeviceCipherHis deviceCipherHis);


    /**
     * 新增数据
     *
     * @param deviceCipherHis 实例对象
     * @return 实例对象
     */
    int insert(DeviceCipherHis deviceCipherHis);

    /**
     * 修改数据
     *
     * @param deviceCipherHis 实例对象
     * @return 实例对象
     */
    int update(DeviceCipherHis deviceCipherHis);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
