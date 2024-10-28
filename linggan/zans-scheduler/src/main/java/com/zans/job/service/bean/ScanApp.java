package com.zans.job.service.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xv
 * @since 2020/5/15 10:28
 */
@Component
@ConfigurationProperties(prefix = "upgrade.app.scan")
public class ScanApp extends BaseApp {
}
