package com.zans.mms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AssetConstants {

	/**
	 * 定义设备类型请求字段
	 */

	/**
	 * 电警
	 */
	public static String ELECTRIC_POLICE="electricPolice";

	/**
	 * 电子标识
	 */
	public static String ELECTRONIC_LABEL="electronicLabel";

	/**
	 * 监控
	 */
	public static String MONITOR="monitor";

	/**
	 * 卡口
	 */
	public static String BAYONET="bayonet";

	/**
	 * 信号机
	 */
	public static String TRAFFIC_SIGNAL="trafficSignal";

	/**
	 * 诱导屏
	 */
	public static String INDUCTION_SCREEN="inductionScreen";

	/**
	 * 未知
	 */

	public static String UNKNOW="99";

	/**
	 * 定义设备类型名称
	 */

	/**
	 * 电警
	 */
	public static String ELECTRIC_POLICE_NAME="电警";

	/**
	 * 电子标识
	 */
	public static String ELECTRONIC_LABEL_NAME="电子标识";

	/**
	 * 监控
	 */
	public static String MONITOR_NAME="监控";

	/**
	 * 卡口
	 */
	public static String BAYONET_NAME="卡口";

	/**
	 * 信号机
	 */
	public static String TRAFFIC_SIGNAL_NAME="红绿灯信号机";

	/**
	 * 诱导屏
	 */
	public static String INDUCTION_SCREEN_NAME="诱导屏";


	public static final String ASSET_START_TIME_SUFFIX = " 08:00:00";
	public static final String ASSET_END_TIME_SUFFIX = " 10:00:00";

	/**
	 * 视频诊断队列
	 */
	public static final String ASSET_DIAGNOSIS_QUEUE="diagnosis";
}
