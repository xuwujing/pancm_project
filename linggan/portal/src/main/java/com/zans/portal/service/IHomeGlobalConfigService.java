package com.zans.portal.service;

import com.zans.base.vo.ApiResult;

/**
 * 首页全局配置表(HomeGlobalConfig)表服务接口
 *
 * @author beixing
 * @since 2021-10-21 10:37:02
 */
public interface IHomeGlobalConfigService {




    ApiResult globalConfig(Integer id);


    ApiResult getIndex(Integer id);
}
