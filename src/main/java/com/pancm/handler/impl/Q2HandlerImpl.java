package com.pancm.handler.impl;

import com.pancm.handler.QHandler;
import org.springframework.stereotype.Service;

@Service
public class Q2HandlerImpl implements QHandler {
    /**
     * 获取优先级
     *
     * @return
     */
    @Override
    public int getPriority() {
        return 2;
    }

    /**
     * 获取类型
     *
     * @return
     */
    @Override
    public Integer getType() {
        return 2;
    }

    @Override
    public String getName(String s,Integer type) {
        return null;
    }
}
