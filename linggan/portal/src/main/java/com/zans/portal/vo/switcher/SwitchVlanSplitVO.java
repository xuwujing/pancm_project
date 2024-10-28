package com.zans.portal.vo.switcher;

import lombok.Data;

@Data
public class SwitchVlanSplitVO {
    
    private String name;

    private String sysName;
    
    private String config;

    /**
     * 交换机ID
     */
    private Integer swId;

    /**
     * 交换机描述，扫描
     */
    private String sysDesc;


}
