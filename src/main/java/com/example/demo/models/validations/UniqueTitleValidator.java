package com.example.demo.models.validations;

import com.example.demo.services.AuthenticationService;
import com.example.demo.services.ContestService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle, String> {
    private final ContestService contestService;

        public UniqueTitleValidator(ContestService contestService) {
        this.contestService = contestService;
    }

    @Override
    public void initialize(UniqueTitle constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.contestService.findByTitle(value).isEmpty();
    }
}
