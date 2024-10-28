package com.zans.portal.vo.switcher;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class SwitcherVlanConfigRespVO {
    private Integer swId;

    private String name;

    private String sysName;

    private Integer vlan;

}
