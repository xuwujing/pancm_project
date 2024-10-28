package com.zans.service;

import com.zans.vo.ApiResult;

/**
 * 首页全局配置表(HomeGlobalConfig)表服务接口
 *
 * @author beixing
 * @since 2021-10-21 10:37:02
 */
public interface IHomeGlobalConfigService {




    ApiResult globalConfig();


    ApiResult getPie();

    ApiResult getLine();


    ApiResult getIndex(Integer id);
}
