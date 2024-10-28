package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.mms.WechatConfigDao;
import com.zans.mms.model.WechatConfig;
import com.zans.mms.service.IWechatConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 微信配置文件表(WechatConfig)表服务实现类
 *
 * @author beixing
 * @since 2021-03-11 11:56:13
 */
@Service("wechatConfigService")
public class WechatConfigServiceImpl implements IWechatConfigService {
    @Resource
    private WechatConfigDao wechatConfigDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public WechatConfig queryById(Long id) {
        return this.wechatConfigDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(WechatConfig wechatConfig) {
        int pageNum = wechatConfig.getPageNum();
        int pageSize = wechatConfig.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);

        List<WechatConfig> result = wechatConfigDao.queryAll(wechatConfig);

        return ApiResult.success(new PageResult<WechatConfig>(page.getTotal(), result, pageNum, pageSize));

    }

    /**
     * 新增数据
     *
     * @param wechatConfig 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(WechatConfig wechatConfig) {
        return wechatConfigDao.insert(wechatConfig);
    }

    /**
     * 修改数据
     *
     * @param wechatConfig 实例对象
     * @return 实例对象
     */
    @Override
    public int update(WechatConfig wechatConfig) {
        return wechatConfigDao.update(wechatConfig);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.wechatConfigDao.deleteById(id) > 0;
    }
}
