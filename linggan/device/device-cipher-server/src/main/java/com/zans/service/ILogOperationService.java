package com.zans.service;

import com.zans.model.LogOperation;
import com.zans.vo.ApiResult;

/**
 * (LogOperation)表服务接口
 *
 * @author beixing
 * @since 2021-03-22 11:57:20
 */
public interface ILogOperationService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    LogOperation queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param logOperation 实例对象
     * @return 对象列表
     */
    ApiResult list(LogOperation logOperation);


    /**
     * 新增数据
     *
     * @param logOperation 实例对象
     * @return 实例对象
     */
    int insert(LogOperation logOperation);

    /**
     * 修改数据
     *
     * @param logOperation 实例对象
     * @return 实例对象
     */
    int update(LogOperation logOperation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
