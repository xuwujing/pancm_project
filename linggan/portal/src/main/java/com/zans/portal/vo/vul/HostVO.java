package com.zans.portal.vo.vul;

import lombok.Data;

import java.util.List;

/**
 * 主机
 * @author xv
 * @since 2021/11/21 11:31
 */
@Data
public class HostVO {

    private Integer hostId;

    private String hostIp;

    private String operationSystem;

    private Integer critical;

    private Integer high;

    private Integer medium;

    private Integer low;

    private Integer info;

    private Integer count;

    private Integer score;

    private String level;

    private List<VulnerabilityVO> vulns;


}
