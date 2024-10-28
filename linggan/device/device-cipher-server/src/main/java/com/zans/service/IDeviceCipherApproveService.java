package com.zans.service;

import com.zans.model.DeviceCipherApprove;
import com.zans.vo.ApiResult;
import com.zans.vo.DeviceCipherApproveVO;

/**
 * (DeviceCipherApprove)表服务接口
 *
 * @author beixing
 * @since 2021-08-23 16:23:48
 */
public interface IDeviceCipherApproveService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceCipherApprove queryById(Integer id);


    /**
     * 新增数据
     *
     * @param deviceCipherApprove 实例对象
     * @return 实例对象
     */
    int insert(DeviceCipherApprove deviceCipherApprove);

    /**
     * 修改数据
     *
     * @param deviceCipherApprove 实例对象
     * @return 实例对象
     */
    int update(DeviceCipherApprove deviceCipherApprove);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    ApiResult approvalList(DeviceCipherApproveVO deviceCipherApproveVO);



}
