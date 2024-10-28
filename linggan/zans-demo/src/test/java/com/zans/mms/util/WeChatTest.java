package com.zans.mms.util;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.util.DateHelper;
import com.zans.mms.service.IWeChatReqService;
import com.zans.mms.vo.wechat.WeChatPushReqVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pancm
 * @Title: zans-mms-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/3/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeChatTest {

    @Autowired
    private IWeChatReqService weChatReqService;

    @Test
    public void test1(){
        WeChatPushReqVO weChatPushReqVO = new WeChatPushReqVO();
        weChatPushReqVO.setCreator("beipeng");
        weChatPushReqVO.setRoleNum("0501");
        weChatPushReqVO.setTemplateType(12);


        List<String> keywords = new ArrayList<>();
        keywords.add("GD2021031119010926108");
        keywords.add("提交工单");
        keywords.add("王家法");
        keywords.add(DateHelper.getNow());
        keywords.add("工单提交->审批");
        weChatPushReqVO.setKeywords(keywords);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickName","王家法");
        jsonObject.put("orgName","第一包-(毕昇云)");
        jsonObject.put("predictPrice",99.99);
        weChatPushReqVO.setJsonObject(jsonObject);

        weChatReqService.weChatPush(weChatPushReqVO);
    }

//    @Test
    public void test2(){
       String jsCode = "013XMs0w3C6TZV2AjK0w3w9brE4XMs0L";

        System.out.println(weChatReqService.getSessionKey(jsCode));
    }
}
