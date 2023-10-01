package com.flapkap.vendingmachine.validation;

import com.flapkap.vendingmachine.model.UserRole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserRoleValidator implements ConstraintValidator<ValidUserRole, UserRole> {

    @Override
    public boolean isValid(UserRole value, ConstraintValidatorContext context) {
        return value == UserRole.SELLER || value == UserRole.BUYER;
    }
}
