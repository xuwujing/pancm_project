package com.zans.portal.vo.vul;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xv
 * @since 2021/11/21 11:31
 */
@Data
public class ReportVO {

    private String reportId;

    private String name;

    private List<HostVO> hosts;

    private List<VulnerabilityVO> vulnerabilities;

    private List<VulHostVul> vulHostVulList;

    private Integer status;

    private Date startTime;

    private Date endTime;

}
