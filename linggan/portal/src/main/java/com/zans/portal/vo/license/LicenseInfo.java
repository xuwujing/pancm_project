package com.zans.portal.vo.license;

import com.zans.license.vo.LicApplyInfo;

public class LicenseInfo extends LicApplyInfo {
    
    // 证书剩余天数
    private Long remainDay;

    public Long getRemainDay() {
        return remainDay;
    }

    public void setRemainDay(Long remainDay) {
        this.remainDay = remainDay;
    }

    
}
