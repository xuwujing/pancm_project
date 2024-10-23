package com.zans.pojo;

import com.zans.commons.utils.MyTools;
import lombok.Data;

import java.util.List;

/**
 * @author pancm
 * @Title: alertmanage-server
 * @Description: 规则实体类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/21
 */
@Data
public class RuleBean {

    private String groupName;
    private String havingName;
    private String distinctName;
    private String distinctNameCard;
    private String groupJudge;
    private Integer groupJudgeCount;

    private List<RuleSearchBean> ruleSearchList;

    @Override
    public String toString() {
        return MyTools.toString(this);
    }
}
