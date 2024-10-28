package com.zans.mms.vo.wechat;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author pancm
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/3/11
 */
@Data
public class WeChatPushReqVO  implements Serializable {
    private static final long serialVersionUID = -27477630671929084L;

    /** 模板类型 */
    private Integer templateType;

    private String creator;

    /** 需要发送的机构所属的角色，为空则向该机构所有的人发送 */
    private String roleNum;

    /** 需要发送的机构，为空则使用所属人的机构发送 */
    private String orgId;


    /** 推送内容的值 */
    private String first;

    /** */
    private List<String> keywords;

    private String remark;

    /** remark 的值映射 */
    private JSONObject jsonObject;

    /** 推送用户unionId列表值 */
    private List<String> unionIdList;

    /**
     * 后缀 用于小程序的跳转参数
     */
    private String suffix;

    /**
     * 类型 用于区分派工单 验收单的跳转路径
     */
    private String type;
}
