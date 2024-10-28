package com.zans.portal.service;

import com.zans.base.vo.ApiResult;
import com.zans.portal.vo.DbVersionVO;


/**
 * @author beixing
 * @Title: (DbVersion)表服务接口
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 17:54:32
 */
public interface IDbVersionService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DbVersionVO queryById(Long id);

    DbVersionVO queryNewOne();


    /**
     * 通过实体作为筛选条件查询
     *
     * @param dbVersionVO 实例对象
     * @return 对象列表
     */
    ApiResult list(DbVersionVO dbVersionVO);


    /**
     * 新增数据
     *
     * @param dbVersionVO 实例对象
     * @return 实例对象
     */
    int insert(DbVersionVO dbVersionVO);

    /**
     * 修改数据
     *
     * @param dbVersionVO 实例对象
     * @return 实例对象
     */
    int update(DbVersionVO dbVersionVO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
