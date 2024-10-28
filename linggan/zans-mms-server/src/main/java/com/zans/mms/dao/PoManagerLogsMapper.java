package com.zans.mms.dao;

import com.zans.mms.model.PoManager;
import com.zans.mms.model.PoManagerLogs;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:舆情日志持久层
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/8/4
 */
@Repository
public interface PoManagerLogsMapper extends Mapper<PoManagerLogs> {
}
