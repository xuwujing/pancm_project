package com.zans.wechat.message_push.dao;

import com.zans.wechat.message_push.model.Cron;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CronDao {

    String getByType(String type);
}
