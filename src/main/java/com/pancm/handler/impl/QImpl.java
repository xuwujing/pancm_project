package com.pancm.handler.impl;

import cn.smallbun.screw.core.util.StringUtils;
import com.pancm.handler.Q;
import com.pancm.handler.QHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QImpl implements Q {

    @Autowired
    private List<QHandler> qHandler;

    @PostConstruct
    public void priority() {
        qHandler = qHandler.stream().sorted(Comparator.comparingInt(QHandler::getPriority)).collect(Collectors.toList());
    }

    private QHandler handlerByUserType(Integer type) {
        return qHandler.stream()
                .filter(attendanceOrgPersonnelHandlerAO -> attendanceOrgPersonnelHandlerAO.getType().equals(type))
                .findFirst().orElseThrow(() -> new RuntimeException("未找到类型"));
    }



    @Override
    public String getName(String s,Integer type) {
        StringBuffer sb = new StringBuffer();
        // 根据用户类型获取对应的处理器
        for (QHandler handler : qHandler) {
            String name = handler.getName(s,type);
            if(StringUtils.isNotBlank(name)){
                sb.append(name).append(",");
            }
        }
        //单个查询
        // handlerByUserType(type).getName(s,type);

        return sb.toString();
    }
}
