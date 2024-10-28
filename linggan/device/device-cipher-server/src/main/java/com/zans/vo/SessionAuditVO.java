package com.zans.vo;

import lombok.Data;

/**
 * @author qiyi
 * @Title: leakproof-server
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/23
 */
@Data
public class SessionAuditVO {

    private Integer fortReserveId;

    private String playBackIds;

    private String serverName;

    private String serverApplication;

}
