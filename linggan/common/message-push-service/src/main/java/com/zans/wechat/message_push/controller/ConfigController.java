package com.zans.wechat.message_push.controller;


import com.zans.wechat.message_push.model.Config;
import com.zans.wechat.message_push.service.ConfigService;
import com.zans.wechat.message_push.vo.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/wxconfig")
@Slf4j
@Api(tags = "微信配置")
@Validated
public class ConfigController {

    @Autowired
    private ConfigService configService;

    /**
     * 配置信息维护
     * @param config
     */
    @PostMapping(value = "/set",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "配置信息维护", notes = "配置信息维护")
    public ApiResult set(@RequestBody Config config) {
            //如果appid存在则update 否则新增
            if (uniqueAppid(config.getAppid())) {
                configService.update(config);
            }else{
                configService.insert(config);
          }
            return ApiResult.success(config);
    }

    /**
     * 校验数据库中是否已经存在此appid  存在返回true 不存在返回false
     * @return
     */
    public Boolean uniqueAppid(String appid){
        return configService.getUniqueAppid(appid);
    }

    /**
     * 修改配置启用停用接口
     * @param appid 开发者标识
     * @param status 需要修改的最终状态
     * @return
     */
    @PostMapping(value = "/changeStatus",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改配置启用停用接口", notes = "修改配置启用停用接口")
    public ApiResult changeStatus(String appid,int status){
        configService.changeStatus(appid,status);
        return ApiResult.success();


    }


    /**
     * 重载微信配置信息
     * @return
     */
    @RequestMapping(value = "/reloadConfig",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "重新加载微信配置信息", notes = "重新加载微信配置信息")
    @Cacheable(value = "config")
    public ApiResult reloadConfig(){
        //查询最新数据放入缓存
        List<Config> configs = configService.getConfigs();
        return ApiResult.success(configs);
    }



    /**
     * 根据开发者标识返回配置信息 如果appid为空则返回所有
     * @param appid 开发者标识
     * @return
     */
    @RequestMapping(value = "/list",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据appid返回配置信息", notes = "根据appid返回配置信息,如果appid为空，则返回所有数据")
    @Cacheable(value = "config")
    public ApiResult getConfig(@RequestParam(required = false) String appid){
        List<Config> config=configService.getConfig(appid);
        return ApiResult.success(config);
    }
}


