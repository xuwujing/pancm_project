package com.zans.mms.config;

import com.zans.mms.dao.SysConstantItemMapper;
import com.zans.mms.service.IAssetService;
import com.zans.mms.service.IRankingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:资产及点位项目和建设单位同步定时任务
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/10/21
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class AssetScheduleTask implements SchedulingConfigurer {

	@Autowired
	SysConstantItemMapper sysConstantItemMapper;

	@Autowired
	IAssetService assetService;

	@Value("${asset.task:0}")
	private String assetTaskFlag;


	/**
	 * 一天执行一次
	 * @param scheduledTaskRegistrar
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {

		if ("1".equals(assetTaskFlag)) {
			log.info("项目字典提取及点位同步资产项目和建设单位字段定时任务已启动");
			scheduledTaskRegistrar.addTriggerTask(
					//1.添加任务内容(Runnable)
					() -> assetService.synchronousData(),
					//2.设置执行周期(Trigger)
					triggerContext -> {
						//2.1 从数据库获取执行周期
						String cron = sysConstantItemMapper.getConstantValueByKey("asset");
						//2.2 合法性校验.
						if (StringUtils.isEmpty(cron)) {
							// Omitted Code .. 默认设置一天一次
							cron = "0 0 0 1/1 * ? ";
						}
						//2.3 返回执行周期(Date)
						return new CronTrigger(cron).nextExecutionTime(triggerContext);
					}
			);
		}
	}

	}

