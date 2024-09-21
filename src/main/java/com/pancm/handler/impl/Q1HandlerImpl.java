package com.pancm.handler.impl;

import com.pancm.handler.QHandler;
import org.springframework.stereotype.Service;

@Service
public class Q1HandlerImpl implements QHandler {
    /**
     * 获取优先级
     *
     * @return
     */
    @Override
    public int getPriority() {
        return 1;
    }

    /**
     * 获取类型
     *
     * @return
     */
    @Override
    public Integer getType() {
        return 1;
    }

    @Override
    public String getName(String s,Integer type) {
        return null;
    }
}
