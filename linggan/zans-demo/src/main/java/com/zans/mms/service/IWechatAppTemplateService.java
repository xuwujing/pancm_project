package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.model.WechatAppTemplate;

/**
 * 微信app模板配置表(WechatAppTemplate)表服务接口
 *
 * @author beixing
 * @since 2021-03-11 16:49:57
 */
public interface IWechatAppTemplateService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WechatAppTemplate queryById(Long id);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param wechatAppTemplate 实例对象
     * @return 对象列表
     */
    ApiResult list(WechatAppTemplate wechatAppTemplate);


    /**
     * 新增数据
     *
     * @param wechatAppTemplate 实例对象
     * @return 实例对象
     */
    int insert(WechatAppTemplate wechatAppTemplate);

    /**
     * 修改数据
     *
     * @param wechatAppTemplate 实例对象
     * @return 实例对象
     */
    int update(WechatAppTemplate wechatAppTemplate);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
