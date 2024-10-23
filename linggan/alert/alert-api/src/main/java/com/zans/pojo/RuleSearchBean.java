package com.zans.pojo;

import com.zans.commons.utils.MyTools;
import lombok.Data;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 规则查询实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
@Data
public class RuleSearchBean {

    private String searchField;
    private String searchJudge;
    private String searchValue;

    @Override
    public String toString() {
        return MyTools.toString(this);
    }
}
