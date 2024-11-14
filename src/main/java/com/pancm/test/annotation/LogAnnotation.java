package com.pancm.test.annotation;

import java.lang.annotation.*;


/**
 * @Author pancm
 * @Description 日志记录注解
 * @Date  2020/8/13
 * @Param 
 * @return 
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LogAnnotation {

	/**
	 * 业务主键
	 * @return
	 */
	String businessNo() default "";

	int businessNoIndex() default -1;

	boolean log() default true;
}
