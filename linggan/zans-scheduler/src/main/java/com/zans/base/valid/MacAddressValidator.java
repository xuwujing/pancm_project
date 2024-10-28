package com.zans.base.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MacAddressValidator implements ConstraintValidator<MacAddress, String> {

    public static final String MAC_PATTERN = "([A-Fa-f0-9]{2} ){5}[A-Fa-f0-9]{2}";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // 这是真正的Mac地址，正则表达式
        return value.matches(MAC_PATTERN);
    }
}
