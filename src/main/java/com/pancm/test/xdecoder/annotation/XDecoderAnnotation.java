package com.pancm.test.xdecoder.annotation;



import java.lang.annotation.*;


/**
 * @Author pancm
 * @Description 加解密注解
 * @Date 2020/8/13
 * @Param
 * @return
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface XDecoderAnnotation {


}
