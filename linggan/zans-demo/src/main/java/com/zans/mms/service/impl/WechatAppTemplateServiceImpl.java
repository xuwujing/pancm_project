package com.zans.mms.service.impl;

import com.zans.base.vo.ApiResult;
import com.zans.mms.dao.mms.WechatAppTemplateDao;
import com.zans.mms.model.WechatAppTemplate;
import com.zans.mms.service.IWechatAppTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 微信app模板配置表(WechatAppTemplate)表服务实现类
 *
 * @author beixing
 * @since 2021-03-11 16:49:57
 */
@Service("wechatAppTemplateService")
public class WechatAppTemplateServiceImpl implements IWechatAppTemplateService {
    @Resource
    private WechatAppTemplateDao wechatAppTemplateDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public WechatAppTemplate queryById(Long id) {
        return this.wechatAppTemplateDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(WechatAppTemplate wechatAppTemplate) {

        return ApiResult.success( wechatAppTemplateDao.queryAll(wechatAppTemplate));

    }

    /**
     * 新增数据
     *
     * @param wechatAppTemplate 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(WechatAppTemplate wechatAppTemplate) {
        return wechatAppTemplateDao.insert(wechatAppTemplate);
    }

    /**
     * 修改数据
     *
     * @param wechatAppTemplate 实例对象
     * @return 实例对象
     */
    @Override
    public int update(WechatAppTemplate wechatAppTemplate) {
        return wechatAppTemplateDao.update(wechatAppTemplate);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.wechatAppTemplateDao.deleteById(id) > 0;
    }
}
