package com.zans.base.valid;

import com.zans.base.util.StringHelper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IpAddressValidator implements ConstraintValidator<IPAddress, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return StringHelper.isValidIp(value);
    }
}
