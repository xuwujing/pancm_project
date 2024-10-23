package com.zans.wechat.message_push.dao;


import com.zans.wechat.message_push.model.Template;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TemplateDao {

    int insert(Template template);

    int update(Template template);
}
