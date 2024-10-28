package com.zans.mms.syslog;

/**
 * @author xv
 * @since 2022/6/29 9:53
 */
public interface ISyslogProcessor {

    boolean processMessage(SyslogEntity syslogEntity);
}
