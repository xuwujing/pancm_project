package com.zans.portal.service;

import com.zans.base.vo.ApiResult;
import com.zans.portal.vo.RadiusEndpointWhiteVO;


/**
 * @author beixing
 * @Title: 设备白名单表(RadiusEndpointWhite)表服务接口
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2022-02-16 18:22:34
 */
public interface IRadiusEndpointWhiteService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RadiusEndpointWhiteVO queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param radiusEndpointWhiteVO 实例对象
     * @return 对象列表
     */
    ApiResult list(RadiusEndpointWhiteVO radiusEndpointWhiteVO);


    /**
     * 新增数据
     *
     * @param radiusEndpointWhiteVO 实例对象
     * @return 实例对象
     */
    int insert(RadiusEndpointWhiteVO radiusEndpointWhiteVO);

    /**
     * 修改数据
     *
     * @param radiusEndpointWhiteVO 实例对象
     * @return 实例对象
     */
    int update(RadiusEndpointWhiteVO radiusEndpointWhiteVO);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    ApiResult remove(RadiusEndpointWhiteVO radiusEndpointWhiteVO);


    ApiResult refresh(RadiusEndpointWhiteVO radiusEndpointWhiteVO);
}
