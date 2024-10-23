package com.zans.wechat.message_push.controller;



import com.zans.wechat.message_push.service.*;
import com.zans.wechat.message_push.vo.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/push")
@Slf4j
@Api(tags = "微信配置")
@Validated
public class PushController  {

   @Autowired
   private PushService pushService;

   @Autowired
   private UserService userService;


    /**
     * 微信公众号模板消息推送
     * 必传的参数：模板id、
     * 所属公众号唯一标识(可以使用appid)、
     * 推送用户id（这里建议使用unionid）、推送的消息。
     * 赋值到模板中的值 params
     */
    @PostMapping(value = "/send",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "微信模板消息发送", notes = "微信模板消息发送")
    public ApiResult  send(String templateId, String appid, List<String> unionids, Map<String,Map<String, String>> params){
        /**
         *   params需要的数据格式如下 依据模板变化
         * Map<String,Map<String, String>> params = new HashMap<>();
         *         Map<String, String> name = new HashMap<>();
         *         name.put("value","100");
         *         params.put("name",name);
         *         Map<String, String> location = new HashMap<String, String>();
         *         location.put("value","东土大唐");
         *         params.put("location",location);
         *         Map<String, String> company = new HashMap<String, String>();
         *         company.put("value","程序猿");
         *         params.put("company",company);
         */
        return pushService.send(templateId,appid,unionids,params);

    }




    @PostMapping("/sendTest")
    public ApiResult sendTest(){
        List<String> unionid=userService.getUnionid();
        Map<String, Map<String,String>> map =new HashMap<>();
        Map<String, String> first = new HashMap<>();
        first.put("value","模板消息推送测试");
        map.put("first",first);
        Map<String, String> keyword1 = new HashMap<>();
        keyword1.put("value","这里是巡检项目");
        map.put("keyword1",keyword1);
        Map<String, String> keyword2 = new HashMap<>();
        keyword2.put("value","这里是巡检任务");
        map.put("keyword2",keyword2);
        Map<String, String> keyword3 = new HashMap<>();
        keyword3.put("value","这里是任务状态");
        map.put("keyword3",keyword3);
        Map<String, String> remark = new HashMap<>();
        remark.put("value","这里是备注，当前时间"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("remark",remark);
        return send("LrwxiinrxAlPsx6V59JQh7_ICQH0xm1i-npVm42TglE","wx6b57c8667d246332",unionid,map);
    }


}
