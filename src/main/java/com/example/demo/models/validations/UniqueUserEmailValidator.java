package com.example.demo.models.validations;


import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AuthenticationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Qualifier;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    private AuthenticationService authenticationService;

    public UniqueUserEmailValidator(@Qualifier("MVCAuthenticationServiceImpl")AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return authenticationService.
                findByEmail(value).
                isEmpty();
    }
}
