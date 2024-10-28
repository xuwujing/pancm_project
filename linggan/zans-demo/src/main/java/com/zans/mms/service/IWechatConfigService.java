package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.model.WechatConfig;

/**
 * 微信配置文件表(WechatConfig)表服务接口
 *
 * @author beixing
 * @since 2021-03-11 11:56:13
 */
public interface IWechatConfigService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WechatConfig queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param wechatConfig 实例对象
     * @return 对象列表
     */
    ApiResult list(WechatConfig wechatConfig);


    /**
     * 新增数据
     *
     * @param wechatConfig 实例对象
     * @return 实例对象
     */
    int insert(WechatConfig wechatConfig);

    /**
     * 修改数据
     *
     * @param wechatConfig 实例对象
     * @return 实例对象
     */
    int update(WechatConfig wechatConfig);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
