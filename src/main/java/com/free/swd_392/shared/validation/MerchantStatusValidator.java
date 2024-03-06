package com.free.swd_392.shared.validation;

import com.free.swd_392.enums.MerchantStatus;
import com.free.swd_392.shared.validation.constraintvalidators.OneOfValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = OneOfValidator.class)
public @interface MerchantStatusValidator {

    String message() default "Value must match one of the values in the list";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    MerchantStatus[] value() default {};

    boolean nullable() default false;
}
