package com.zans.portal.service.impl;

import com.zans.base.config.EnumErrorCode;
import com.zans.base.util.DateHelper;
import com.zans.base.vo.ApiResult;
import com.zans.license.util.LicenseUtils;
import com.zans.license.util.MachineIdentityUtil;
import com.zans.license.vo.LicApplyInfo;
import com.zans.portal.config.GlobalConstants;
import com.zans.portal.model.SysConstant;
import com.zans.portal.service.ILicenseService;
import com.zans.portal.service.ISysConstantService;
import com.zans.portal.vo.license.LicenseInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class LicenseServiceImpl implements ILicenseService {

    @Autowired
    ISysConstantService sysConstantService;

    private static String licenseConstantKey = "license";

    @Override
    public LicenseInfo getLicenseInfo() {
        SysConstant sysConstant = sysConstantService.findConstantByKey(licenseConstantKey);
        if (sysConstant != null) {
            String license = sysConstant.getConstantValue();
            try {
                LicApplyInfo info = LicenseUtils.verifyLicense(license, GlobalConstants.RSA_PUBLIC_KEY);
                if (info != null) {
                    LicenseInfo licenseInfo = new LicenseInfo();
                    BeanUtils.copyProperties(info, licenseInfo);
                    licenseInfo.setRemainDay(DateHelper.betweenDays(DateHelper.getToday(), info.getDateLimit()));
                    // 验证机器码
                    // String sn = MachineIdentityUtil.getMachineIdentity();
                    // if (sn.equals(licenseInfo.getSerialNumber())){
                    // return licenseInfo;
                    // }
                    return licenseInfo;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("授权码取值错误！原因是:",e);
            }
        }
        return null;
    }

    @Override
    public ApiResult changeLicense(String license) {
        if (StringUtils.hasText(license)) {
            try {
                LicApplyInfo info = LicenseUtils.verifyLicense(license, GlobalConstants.RSA_PUBLIC_KEY);
                if (info != null) {
                    String sn = MachineIdentityUtil.getMachineIdentity();
                    if (sn.equals(info.getSerialNumber())) {
                        LicenseInfo licenseInfo = new LicenseInfo();
                        BeanUtils.copyProperties(info, licenseInfo);
                        licenseInfo.setRemainDay(DateHelper.betweenDays(DateHelper.getToday(), info.getDateLimit()));
                        SysConstant sysConstant = sysConstantService.findConstantByKey(licenseConstantKey);
                        sysConstant.setConstantValue(license);
                        sysConstantService.updateSelective(sysConstant);
                        return ApiResult.success(licenseInfo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ApiResult.error(EnumErrorCode.LICENSE_ERROR);
    }

}
