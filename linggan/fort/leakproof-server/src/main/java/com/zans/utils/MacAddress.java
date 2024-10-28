package com.zans.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author xv
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER,})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MacAddressValidator.class)
public @interface MacAddress {

    String message() default "{MAC必须是17位，用空格连接符}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
