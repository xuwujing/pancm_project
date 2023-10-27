/**
 * 
 */
package com.pancm.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The interface Check.
 *
 * @author pancm
 * @Title: Check
 * @Description: 校验的注解
 * @Version:1.0.0
 * @date 2018年5月10日
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RUNTIME)
public @interface Check {

    /**
     * Value string [ ].
     *
     * @return the string [ ]
     */
    String[] value();

}
