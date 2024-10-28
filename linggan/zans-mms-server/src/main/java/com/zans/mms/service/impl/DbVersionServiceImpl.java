package com.zans.mms.service.impl;



import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.DbVersionDao;
import com.zans.mms.model.DbVersion;
import com.zans.mms.service.IDbVersionService;
import com.zans.mms.vo.DbVersionVO;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author beixing
 * @Title: (DbVersion)表服务实现类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-06-23 17:54:33
 */
@Service("dbVersionService")
public class DbVersionServiceImpl implements IDbVersionService {
    @Resource
    private DbVersionDao dbVersionDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DbVersionVO queryById(Long id) {
        return this.dbVersionDao.queryById(id);
    }

    @Override
    public DbVersionVO queryNewOne() {
        return dbVersionDao.queryNewOne();
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(DbVersionVO dbVersion) {
        int pageNum = dbVersion.getPageNum();
        int pageSize = dbVersion.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<DbVersionVO> result = dbVersionDao.queryAll(dbVersion);
        return ApiResult.success(new PageResult<>(page.getTotal(), result, pageSize, pageNum));

    }

    /**
     * 新增数据
     *
     * @param dbVersionVO 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(DbVersionVO dbVersionVO) {
        DbVersion dbVersion = new DbVersion();
        BeanUtils.copyProperties(dbVersionVO, dbVersion);
        return dbVersionDao.insert(dbVersion);
    }

    /**
     * 修改数据
     *
     * @param dbVersionVO 实例对象
     * @return 实例对象
     */
    @Override
    public int update(DbVersionVO dbVersionVO) {
        DbVersion dbVersion = new DbVersion();
        BeanUtils.copyProperties(dbVersionVO, dbVersion);
        return dbVersionDao.update(dbVersion);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.dbVersionDao.deleteById(id) > 0;
    }
}
