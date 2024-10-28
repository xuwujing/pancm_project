package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.mms.WechatUserWxbindDao;
import com.zans.mms.model.WechatUserWxbind;
import com.zans.mms.service.IWechatUserWxbindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * (WechatUserWxbind)表服务实现类
 *
 * @author beixing
 * @since 2021-03-07 17:03:26
 */
@Service("wechatUserWxbindService")
public class WechatUserWxbindServiceImpl implements IWechatUserWxbindService {
    @Resource
    private WechatUserWxbindDao wechatUserWxbindDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public WechatUserWxbind queryById(Integer id) {
        return this.wechatUserWxbindDao.queryById(id);
    }

    @Override
    public WechatUserWxbind queryByOpenId(String appId, String openId) {
        return this.wechatUserWxbindDao.queryByOpenId(appId, openId);
    }

    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(WechatUserWxbind wechatUserWxbind) {
        int pageNum = wechatUserWxbind.getPageNum();
        int pageSize = wechatUserWxbind.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);

        List<WechatUserWxbind> result = wechatUserWxbindDao.queryAll(wechatUserWxbind);

        return ApiResult.success(new PageResult<WechatUserWxbind>(page.getTotal(), result, pageNum, pageSize));

    }

    /**
     * 新增数据
     *
     * @param wechatUserWxbind 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(WechatUserWxbind wechatUserWxbind) {
        return wechatUserWxbindDao.insert(wechatUserWxbind);
    }

    /**
     * 修改数据
     *
     * @param wechatUserWxbind 实例对象
     * @return 实例对象
     */
    @Override
    public int update(WechatUserWxbind wechatUserWxbind) {
        return wechatUserWxbindDao.update(wechatUserWxbind);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.wechatUserWxbindDao.deleteById(id) > 0;
    }

    @Override
    public int deleteByUserName(String userName) {
        return wechatUserWxbindDao.deleteByUserName(userName);
    }
}
