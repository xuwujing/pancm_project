package com.zans.vo;

import com.zans.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xv
 * @since 2020/3/21 23:40
 */
@Slf4j
public class NotEmptyValidator implements IConstraintValidator {

    @Override
    public boolean isValid(Object var1) {
        if (var1 == null) {
            return false;
        }
        String val = null;
        if (var1 instanceof String) {
            val = (String) var1;
        } else {
            val = var1.toString();
        }
        boolean r = StringHelper.isNotBlank(val) && !val.contains("[必填]");
        return r;
    }

    @Override
    public String getMessage() {
        return "必填";
    }
}
