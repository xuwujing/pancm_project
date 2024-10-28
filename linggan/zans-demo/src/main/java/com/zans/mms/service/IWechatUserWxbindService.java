package com.zans.mms.service;

import com.zans.base.vo.ApiResult;
import com.zans.mms.model.WechatUserWxbind;

/**
 * (WechatUserWxbind)表服务接口
 *
 * @author beixing
 * @since 2021-03-07 17:03:26
 */
public interface IWechatUserWxbindService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WechatUserWxbind queryById(Integer id);

    WechatUserWxbind queryByOpenId(String appId, String openId);
    /**
     * 通过实体作为筛选条件查询
     *
     * @param wechatUserWxbind 实例对象
     * @return 对象列表
     */
    ApiResult list(WechatUserWxbind wechatUserWxbind);


    /**
     * 新增数据
     *
     * @param wechatUserWxbind 实例对象
     * @return 实例对象
     */
    int insert(WechatUserWxbind wechatUserWxbind);

    /**
     * 修改数据
     *
     * @param wechatUserWxbind 实例对象
     * @return 实例对象
     */
    int update(WechatUserWxbind wechatUserWxbind);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    int deleteByUserName(String userName);

}
