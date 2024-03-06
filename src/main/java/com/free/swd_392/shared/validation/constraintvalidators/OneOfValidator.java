package com.free.swd_392.shared.validation.constraintvalidators;

import com.free.swd_392.enums.MerchantStatus;
import com.free.swd_392.shared.validation.MerchantStatusValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class OneOfValidator implements ConstraintValidator<MerchantStatusValidator, MerchantStatus> {

    private boolean nullable;
    private Set<MerchantStatus> value;

    @Override
    public void initialize(MerchantStatusValidator oneOf) {
        value = Arrays.stream(oneOf.value()).collect(Collectors.toUnmodifiableSet());
        nullable = oneOf.nullable();
    }

    @Override
    public boolean isValid(MerchantStatus str, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (str == null) {
            if (nullable) {
                return true;
            }
            context.buildConstraintViolationWithTemplate("Value can not be null")
                    .addConstraintViolation();
            return false;
        }
        context.buildConstraintViolationWithTemplate("Value must be in: " + value)
                .addConstraintViolation();
        return CollectionUtils.isEmpty(value)
                || value.contains(str);
    }
}
