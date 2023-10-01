package com.flapkap.vendingmachine.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserRoleValidator.class)
public @interface ValidUserRole {

    String message() default "Invalid role. Role should be SELLER or BUYER.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
