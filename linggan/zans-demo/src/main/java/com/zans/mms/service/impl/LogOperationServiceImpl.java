package com.zans.mms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zans.base.vo.ApiResult;
import com.zans.base.vo.PageResult;
import com.zans.mms.dao.mms.LogOperationDao;
import com.zans.mms.model.LogOperation;
import com.zans.mms.service.ILogOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * (LogOperation)表服务实现类
 *
 * @author beixing
 * @since 2021-03-22 11:57:20
 */
@Service("logOperationService")
public class LogOperationServiceImpl implements ILogOperationService {
    @Resource
    private LogOperationDao logOperationDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public LogOperation queryById(Long id) {
        return this.logOperationDao.queryById(id);
    }


    /**
     * 根据条件查询
     *
     * @return 实例对象的集合
     */
    @Override
    public ApiResult list(LogOperation logOperation) {
        int pageNum = logOperation.getPageNum();
        int pageSize = logOperation.getPageSize();
        Page page = PageHelper.startPage(pageNum, pageSize);

        List<LogOperation> result = logOperationDao.queryAll(logOperation);

        return ApiResult.success(new PageResult<LogOperation>(page.getTotal(), result, pageNum, pageSize));

    }

    /**
     * 新增数据
     *
     * @param logOperation 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(LogOperation logOperation) {
        return logOperationDao.insert(logOperation);
    }

    /**
     * 修改数据
     *
     * @param logOperation 实例对象
     * @return 实例对象
     */
    @Override
    public int update(LogOperation logOperation) {
        return logOperationDao.update(logOperation);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.logOperationDao.deleteById(id) > 0;
    }
}