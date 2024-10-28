package com.zans.vo;

/**
 * @author xv
 * @since 2020/3/21 23:35
 */
public interface IConstraintValidator {

    boolean isValid(Object var1);

    String getMessage();
}
