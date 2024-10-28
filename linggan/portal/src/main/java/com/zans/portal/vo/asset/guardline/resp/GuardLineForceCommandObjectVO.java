package com.zans.portal.vo.asset.guardline.resp;

import lombok.Data;

/**
 * 警卫线路强制下线对象
 */
@Data
public class GuardLineForceCommandObjectVO {

    private Integer id;

    private Integer endpointId;

    private Integer alive;

    /**
     * 命令执行结果
     */
    private Integer commandExecuteStatus;

    private String ipAddr;




}
