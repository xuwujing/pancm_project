package com.zans.portal.vo.ip;

import lombok.Builder;
import lombok.Data;

/**
 * IP分配对象
 * @author xv
 * @since 2020/3/23 11:40
 */
@Builder
@Data
public class IpAssign {

    private Integer row;

    private String pointName;

    private String ip;

    private String mask;

    private String gateway;

    private Integer vlan;
}
