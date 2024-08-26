package com.example.demo.models.validations;

import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AuthenticationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

   private final AuthenticationService authenticationService;

    public UniqueUsernameValidator(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.authenticationService.findByUsername(value).isEmpty();
    }
}
