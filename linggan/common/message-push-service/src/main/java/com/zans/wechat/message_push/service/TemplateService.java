package com.zans.wechat.message_push.service;

import com.zans.wechat.message_push.model.Template;

public interface TemplateService {
    boolean insert(Template template);

    boolean update(Template template);
}
