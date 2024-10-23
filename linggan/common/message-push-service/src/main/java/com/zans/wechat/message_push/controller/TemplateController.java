package com.zans.wechat.message_push.controller;

import com.zans.wechat.message_push.model.Template;
import com.zans.wechat.message_push.service.TemplateService;
import com.zans.wechat.message_push.vo.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/wxtemplate")
@Api(tags = "模板管理")
@Validated
public class TemplateController {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TemplateService templateService;

    /**
     * 新增或修改模板
     * @param template
     * @return
     */
    @ApiOperation(value = "消息模板维护", notes = "消息模板维护")
    @PostMapping(value = "/set",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult<Template> set(@RequestBody  Template template){
        //判断id是否存在 不存在则新增 存在则修改
        if(template.getId()==null){
            templateService.insert(template);
        }else{
            templateService.update(template);
        }
        return ApiResult.success(template);
    }



}
