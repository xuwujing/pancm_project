package com.zans.wechat.message_push.service.impl;

import com.zans.wechat.message_push.dao.TemplateDao;
import com.zans.wechat.message_push.model.Template;
import com.zans.wechat.message_push.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateDao templateDao;

    @Override
    public boolean insert(Template template) {

        template.setCreateTime(new Date());
        template.setUpdateTime(new Date());
        return templateDao.insert(template)>0;
    }

    @Override
    public boolean update(Template template) {
        template.setUpdateTime(new Date());
        return templateDao.update(template)>0;
    }
}
