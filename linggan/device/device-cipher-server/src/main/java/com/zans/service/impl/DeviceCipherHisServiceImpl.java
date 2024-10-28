package com.zans.service.impl;

import com.zans.model.DeviceCipherHis;
import com.zans.dao.DeviceCipherHisDao;
import com.zans.service.IDeviceCipherHisService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.vo.ApiResult;
import com.zans.vo.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * (DeviceCipherHis)表服务实现类
 *
 * @author beixing
 * @since 2021-08-23 16:15:55
 */
@Service("deviceCipherHisService")
public class DeviceCipherHisServiceImpl implements IDeviceCipherHisService {
    @Resource
    private DeviceCipherHisDao deviceCipherHisDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DeviceCipherHis queryById(Integer id) {
        return this.deviceCipherHisDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(DeviceCipherHis deviceCipherHis) {
        int pageNum = deviceCipherHis.getPageNum();
        int pageSize = deviceCipherHis.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);

        List<DeviceCipherHis> result = deviceCipherHisDao.queryAll(deviceCipherHis);

        return ApiResult.success(new PageResult<DeviceCipherHis>(page.getTotal(), result, pageSize, pageNum));

    }

    /**
     * 新增数据
     *
     * @param deviceCipherHis 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(DeviceCipherHis deviceCipherHis) {
        return deviceCipherHisDao.insert(deviceCipherHis);
    }

    /**
     * 修改数据
     *
     * @param deviceCipherHis 实例对象
     * @return 实例对象
     */
    @Override
    public int update(DeviceCipherHis deviceCipherHis) {
        return deviceCipherHisDao.update(deviceCipherHis);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.deviceCipherHisDao.deleteById(id) > 0;
    }
}
