package com.zans.portal.vo.switcher;

import lombok.Data;

@Data
public class SwitcherOnlineReportVO {

    private String beginTime;
    private String endTime;
    private Integer offlineType;//0.断网,1断电

    public Integer getOfflineType() {
        return offlineType==null?0:offlineType;
    }
}
