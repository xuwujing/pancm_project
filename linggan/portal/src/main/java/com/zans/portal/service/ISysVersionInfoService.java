package com.zans.portal.service;

import com.zans.base.vo.ApiResult;
import com.zans.portal.vo.version.SysVersionInfoVO;


/**
 * @author beixing
 * @Title: (SysVersionInfo)表服务接口
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 14:25:23
 */
public interface ISysVersionInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysVersionInfoVO queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param
     * @return 对象列表
     */
    ApiResult list();




}
