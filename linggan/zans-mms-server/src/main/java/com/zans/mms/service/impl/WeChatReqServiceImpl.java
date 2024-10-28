package com.zans.mms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.util.HttpClientUtil;
import com.zans.base.util.MsgFormatUtil;
import com.zans.base.util.RestTemplateHelper;
import com.zans.base.util.WxUtil;
import com.zans.base.vo.ApiResult;
import com.zans.mms.config.PoolTaskConfig;
import com.zans.mms.dao.SysUserDao;
import com.zans.mms.dao.WechatAppTemplateDao;
import com.zans.mms.dao.WechatUserWxbindDao;
import com.zans.mms.model.SysUser;
import com.zans.mms.model.WechatAppTemplate;
import com.zans.mms.model.WechatConfig;
import com.zans.mms.service.IWeChatReqService;
import com.zans.mms.service.IWechatConfigService;
import com.zans.mms.vo.wechat.WeChatPushReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pancm
 * @Title: zans-mms-server
 * @Description: 微信消息推送
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/3/11
 */
@Slf4j
@Service("weChatReqService")
public class WeChatReqServiceImpl implements IWeChatReqService {

    @Value("${request.wechat.url}")
    private  String weChatUrl;

    @Autowired
    private RestTemplateHelper rest;

    @Autowired
    private IWechatConfigService wechatConfigService;

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private WechatUserWxbindDao wechatUserWxbindDao;

    @Resource
    private WechatAppTemplateDao wechatAppTemplateDao;


    @Autowired
    private PoolTaskConfig poolTaskConfig;

    @Override
    public ApiResult setConfig() {
        String url = weChatUrl+WECHAT_CONFIG_SET;
        WechatConfig wechatConfig = wechatConfigService.queryById(1L);
        Map<String,Object> postMap = new HashMap<>();
        postMap.put("groupId",1);
        postMap.put("name",wechatConfig.getMdAppname());
        postMap.put("appid",wechatConfig.getMdAppid());
        postMap.put("sercet",wechatConfig.getMdSecret());
        postMap.put("type",0);
        postMap.put("enableStatus",1);
        poolTaskConfig.executeRadApiRequest(url, JSONObject.toJSONString(postMap));
        postMap.put("name",wechatConfig.getMpAppname());
        postMap.put("appid",wechatConfig.getMpAppid());
        postMap.put("sercet",wechatConfig.getMpSecret());
        postMap.put("type",1);
        poolTaskConfig.executeRadApiRequest(url, JSONObject.toJSONString(postMap));
        return ApiResult.success();
    }

    @Override
    public String getSessionKey(String jsCode) {
        String url = weChatUrl+WECHAT_SESSIONKEY;
        WechatConfig wechatConfig = wechatConfigService.queryById(1L);
        Map<String,Object> postMap = new HashMap<>();
        //微信小程序的appid
        postMap.put("appid",wechatConfig.getMpAppid());
        postMap.put("jsCode",jsCode);
        String result = null;
        try {
            result = HttpClientUtil.post(url,JSONObject.toJSONString(postMap));
        } catch (Exception e) {
            log.error("请求失败！url:{},请求参数:{}",url,JSONObject.toJSONString(postMap));
        }
        return result;
    }

    @Override
    public String decryptMsg(String encryptedData, String sessionKey, String iv) {
        return WxUtil.decryptData(encryptedData,sessionKey,iv);
    }

    @Override
    public ApiResult weChatPush(WeChatPushReqVO weChatPushReqVO) {
        String url = weChatUrl+WECHAT_PUSH_SEND;
        // 通过用户得
        String sourcePerson = weChatPushReqVO.getCreator();
        String orgId = weChatPushReqVO.getOrgId();
        if(StringUtils.isEmpty(orgId)){
            SysUser sysUser = sysUserDao.queryByIdOrUsername(null,sourcePerson);
            orgId = sysUser.getMaintainNum();
        }
        List<String> unionIdList = wechatUserWxbindDao.queryByOrgId(orgId,weChatPushReqVO.getRoleNum(),1,1);
        WechatConfig wechatConfig = wechatConfigService.queryById(1L);
        WechatAppTemplate wechatAppTemplate = new WechatAppTemplate();
        wechatAppTemplate.setTemplateType(weChatPushReqVO.getTemplateType());
        WechatAppTemplate appTemplate = wechatAppTemplateDao.findOne(wechatAppTemplate);
        weChatPushReqVO.setFirst(appTemplate.getTemplateName());
        Map<String, Object> postMap = packageContent(weChatPushReqVO,  unionIdList, wechatConfig, appTemplate);
        poolTaskConfig.executeRadApiRequest(url, JSONObject.toJSONString(postMap));
        return ApiResult.success();
    }

    @Override
    public ApiResult weChatPushWorkFLow(WeChatPushReqVO weChatPushReqVO) {
        String url = weChatUrl+WECHAT_PUSH_SEND;
        List<String> unionIdList = weChatPushReqVO.getUnionIdList();

        WechatConfig wechatConfig = wechatConfigService.queryById(1L);
        WechatAppTemplate wechatAppTemplate = new WechatAppTemplate();
        wechatAppTemplate.setTemplateType(weChatPushReqVO.getTemplateType());
        WechatAppTemplate appTemplate = wechatAppTemplateDao.findOne(wechatAppTemplate);
        weChatPushReqVO.setFirst(appTemplate.getTemplateName());
        Map<String, Object> postMap = packageContent(weChatPushReqVO,  unionIdList, wechatConfig, appTemplate);
        poolTaskConfig.executeRadApiRequest(url, JSONObject.toJSONString(postMap));
        return ApiResult.success();
    }



    /**
     * 消息封装
     * */
    private Map<String, Object> packageContent(WeChatPushReqVO weChatPushReqVO, List<String> unionIdList, WechatConfig wechatConfig, WechatAppTemplate appTemplate) {
        String msg = appTemplate.getTemplateMsg();
        String remark =  MsgFormatUtil.format(msg,weChatPushReqVO.getJsonObject());
        List<String> keywords = weChatPushReqVO.getKeywords();
        Map<String,Map<String,String>> mapMap = new HashMap<>();
        int size = appTemplate.getTemplateRuleSize();
        for (int i = 0; i < size; i++) {
            Map<String,String> map = new HashMap<>();
            if(i == 0){
                map.put(VALUE,weChatPushReqVO.getFirst());
                mapMap.put(FIRST,map);
                continue;
            }
            if(i == (size-1)){
                map.put(VALUE,remark);
                mapMap.put(REMARK,map);
                break;
            }
            map.put(VALUE,keywords.get(i-1));
            mapMap.put(KEYWORD+i,map);
        }
        Map<String,Object> postMap = new HashMap<>();
        postMap.put(REQ_DATA_TEMPLATEID,appTemplate.getTemplateId());
        postMap.put(REQ_DATA_APPID,wechatConfig.getMdAppid());
        postMap.put(REQ_DATA_UNIONIDS,unionIdList);
        postMap.put(REQ_DATA_PARAMS,mapMap);
        if( appTemplate.getIsRedirect()!=null && appTemplate.getIsRedirect() == 1){
            postMap.put(REQ_DATA_MINI_APPID,wechatConfig.getMpAppid());
        }
        if(!StringUtils.isEmpty(weChatPushReqVO.getSuffix())){
            postMap.put(SUFFIX,weChatPushReqVO.getSuffix());
        }
        if(!StringUtils.isEmpty(weChatPushReqVO.getType())){
            postMap.put(TYPE,weChatPushReqVO.getType());
        }

        return postMap;
    }


    /**
     *  map的内容
     **/
    private static final String FIRST = "first";
    private static final String KEYWORD = "keyword";
    private static final String REMARK = "remark";
    private static final String VALUE = "value";


    private static final String REQ_DATA_TEMPLATEID = "templateId";
    private static final String REQ_DATA_APPID = "appid";
    private static final String REQ_DATA_UNIONIDS = "unionids";
    private static final String REQ_DATA_PARAMS = "params";
    private static final String REQ_DATA_MINI_APPID = "miniAppid";
    private static final String SUFFIX = "suffix";
    private static final String TYPE = "type";

    private static String WECHAT_SESSIONKEY="/util/getSessionKey";

    private static String WECHAT_DECRYPT="/util/decrypt";

    private static String WECHAT_CONFIG_SET="/wxconfig/set";

    private static String WECHAT_PUSH_SEND ="/push/send";
}
