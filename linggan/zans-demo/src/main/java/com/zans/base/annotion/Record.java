package com.zans.base.annotion;

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
public @interface Record {



	boolean log() default true;

	String opPlatform() default "PC";

	String module() default "";

	String operation() default "";


}
