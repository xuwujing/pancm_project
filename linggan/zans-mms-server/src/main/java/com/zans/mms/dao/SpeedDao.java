package com.zans.mms.dao;

import com.zans.mms.model.PoManager;
import com.zans.mms.model.Speed;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:上传进度及日志记录持久层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/27
 */
@Repository
public interface SpeedDao extends Mapper<Speed> {

}
