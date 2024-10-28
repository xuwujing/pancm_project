package com.zans.mms.syslog;

import lombok.Builder;
import lombok.Data;

/**
 * @author xv
 * @since 2022/6/29 9:48
 */
@Data
@Builder
public class SyslogEntity {

    public SyslogEntity(String message, String sourceIp, int port) {
        this.message = message;
        this.sourceIp = sourceIp;
        this.port = port;
    }

    private String message;

    private String sourceIp;

    private int port;
}
