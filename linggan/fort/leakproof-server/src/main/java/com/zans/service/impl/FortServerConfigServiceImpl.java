package com.zans.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.dao.FortServerConfigDao;
import com.zans.model.FortServerConfig;
import com.zans.service.IFortServerConfigService;
import com.zans.utils.ApiResult;
import com.zans.vo.FortServerConfigVO;
import com.zans.vo.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author beixing
 * @Title: (FortServerConfig)表服务实现类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-28 14:17:37
 */
@Service("fortServerConfigService")
public class FortServerConfigServiceImpl implements IFortServerConfigService {
    @Resource
    private FortServerConfigDao fortServerConfigDao;

    /**
     * 通过ID查询单条数据
     *
     * @param serverId 主键
     * @return 实例对象
     */
    @Override
    public FortServerConfigVO queryById(Integer serverId) {
        return this.fortServerConfigDao.queryById(serverId);
    }

    @Override
    public FortServerConfigVO queryByIp(String serverIp) {
        return fortServerConfigDao.queryByIp(serverIp);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(FortServerConfigVO fortServerConfig) {
        int pageNum = fortServerConfig.getPageNum();
        int pageSize = fortServerConfig.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<FortServerConfigVO> result = fortServerConfigDao.queryAll(fortServerConfig);
        return ApiResult.success(new PageResult<>(page.getTotal(), result, pageSize, pageNum));

    }

    /**
     * 新增数据
     *
     * @param fortServerConfigVO 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(FortServerConfigVO fortServerConfigVO) {
        FortServerConfig fortServerConfig = new FortServerConfig();
        BeanUtils.copyProperties(fortServerConfigVO, fortServerConfig);
        return fortServerConfigDao.insert(fortServerConfig);
    }

    /**
     * 修改数据
     *
     * @param fortServerConfigVO 实例对象
     * @return 实例对象
     */
    @Override
    public int update(FortServerConfigVO fortServerConfigVO) {
        FortServerConfig fortServerConfig = new FortServerConfig();
        BeanUtils.copyProperties(fortServerConfigVO, fortServerConfig);
        return fortServerConfigDao.update(fortServerConfig);
    }

    /**
     * 通过主键删除数据
     *
     * @param serverId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer serverId) {
        return this.fortServerConfigDao.deleteById(serverId) > 0;
    }
}
