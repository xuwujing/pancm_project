package com.zans.wechat.message_push.controller;

import com.zans.wechat.message_push.service.ConfigService;
import com.zans.wechat.message_push.service.UtilService;
import com.zans.wechat.message_push.util.WxUtil;
import com.zans.wechat.message_push.vo.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/util")
@Api(tags = "微信解密")
@Validated
public class UtilController {



    @Autowired
    private UtilService utilService;

    /**
     * 获取sessionKey 等信息
     * @param appid
     * @param js_code
     * @return
     */
    @PostMapping("/getSessionKey")
    @ApiOperation(value = "获取SessionKey", notes = "获取SessionKey")
    public ApiResult getSessionKey(String appid, String js_code){
        //如果发生错误进行解析 未发生错误正常返回
        return  utilService.getSessionKey(appid,js_code);

    }


    /**
     * 微信数据解密
     * @param encryptedData 加密的数据
     * @param sessionKey
     * @param iv 偏移量
     * @return
     */
    @PostMapping("/decrypt")
    @ApiOperation(value = "微信数据解密", notes = "微信数据解密")
    public ApiResult decrypt(String encryptedData,String sessionKey,String iv){
        return ApiResult.success(WxUtil.decryptData(encryptedData,sessionKey,iv));

    }





}
