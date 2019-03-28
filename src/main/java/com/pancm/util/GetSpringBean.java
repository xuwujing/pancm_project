package com.pancm.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * spring动态获取bean实现
 *
 * @author xuwujing
 */
public class GetSpringBean implements ApplicationContextAware{

	private static ApplicationContext context;

    /**
     * Gets bean.
     *
     * @param name the name
     * @return the bean
     */
    public static Object getBean(String name) {
		return context.getBean(name);
	}

    /**
     * Gets bean.
     *
     * @param <T> the type parameter
     * @param c   the c
     * @return the bean
     */
    public static <T> T getBean(Class<T> c) {

		return context.getBean(c);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}

}
