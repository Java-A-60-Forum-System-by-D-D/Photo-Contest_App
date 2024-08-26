package com.example.demo.models.validations;


import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AuthenticationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    private AuthenticationService authenticationService;

    public UniqueUserEmailValidator(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return authenticationService.
                findByEmail(value).
                isEmpty();
    }
}
