package com.zans.portal.vo.msg;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import lombok.Data;

/**
 * 消息查询
 * @author xv
 * @since 2020/3/8 12:32
 */
@Data
public class MsgSearchVO extends BasePage {

    @JSONField(name = "user_name")
    private String userName;

    private Integer module;

    private Integer readed;

    private String title;
}
