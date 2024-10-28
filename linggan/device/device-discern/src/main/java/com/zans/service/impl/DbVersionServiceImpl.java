package com.zans.service.impl;


import com.zans.dao.DbVersionDao;
import com.zans.service.IDbVersionService;
import com.zans.vo.DbVersionVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


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
     * @param
     * @return 实例对象
     */
    @Override
    public DbVersionVO queryNewOne() {
        return dbVersionDao.queryNewOne();
    }


}
