package com.zans.base.office.validator;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xv
 * @since 2020/3/21 23:36
 */
@Slf4j
public class NotNullValidator implements IConstraintValidator {

    @Override
    public boolean isValid(Object var1) {
        return var1 != null && !var1.toString().contains("[必填]");
    }

    @Override
    public String getMessage() {
        return "必填";
    }
}
