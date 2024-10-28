package com.zans.portal.service;

import com.zans.base.vo.ApiResult;
import com.zans.license.vo.LicApplyInfo;
import com.zans.portal.vo.license.LicenseInfo;

/**
 * 授权码相关操作
 */
public interface ILicenseService {
    
    /**
     * 获取License 信息
     * @return
     */
    LicenseInfo getLicenseInfo();

    ApiResult changeLicense(String license);
}
