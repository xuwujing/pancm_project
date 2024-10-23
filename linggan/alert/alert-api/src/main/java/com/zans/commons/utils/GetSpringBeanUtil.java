package com.zans.commons.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 
* @Title: GetSpringBean
* @Description:
* spring动态获取bean实现 
* @Version:1.0.0  
* @author pancm
* @date 2018年5月8日
 */
public class GetSpringBeanUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	public static Object getBean(String name) {
		return context.getBean(name);
	}

	public static <T> T getBean(Class<T> c) {

		return context.getBean(c);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if(applicationContext!=null){
			context = applicationContext;
		}
	}

}
