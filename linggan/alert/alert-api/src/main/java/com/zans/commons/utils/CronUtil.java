package com.zans.commons.utils;

/**
 * @ClassName: CronUtil
 * @Description: Cron表达式工具类 目前支持三种常用的cron表达式 1.每天的某个时间点执行 例:12 12 12 * *
 *               ?表示每天12时12分12秒执行 2.每周的哪几天执行 例:12 12 12 ? * 1,2,3表示每周的周1周2周3
 *               ,12时12分12秒执行 3.每月的哪几天执行 例:12 12 12 1,21,13 * ?表示每月的1号21号13号
 *               12时12分12秒执行
 * @author
 * @date
 *
 */
public class CronUtil {

	/**
	 *
	 * 方法摘要：构建Cron表达式
	 *
	 * @param rate  频率 0秒；1分；2小时；3日；4月
	 * @param cycle 周期
	 * @return String
	 */
	public static String createLoopCronExpression(int rate, int cycle) {
		String cron = "";
		switch (rate) {
			// 每cycle秒执行一次
			case 0:
				cron = "0/" + cycle + " * * * * ?";
				break;
			// 每cycle分钟执行一次
			case 1:
				cron = "0 0/" + cycle + " * * * ?";
				break;
			// 每cycle小时执行一次
			case 2:
				cron = "0 0 0/" + cycle + " * * ?";
				break;
			// 每cycle天的0点执行一次
			case 3:
				cron = "0 0 0 1/" + cycle + " * ?";
				break;
			// 每cycle月的1号0点执行一次
			case 4:
				cron = "0 0 0 1 1/" + cycle + " ? ";
				break;
			//  每天cycle点执行一次
			case 5:
				cron = "0 0 " + cycle+ "  * * ?";
				break;
			// 默认每cycle秒执行一次
			default:
				cron = "0/" + cycle + " * * * * ?";
				break;
		}
		return cron;
	}
}
