package com.example.membershiplibrary.annotation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthorNameValidator implements ConstraintValidator<ValidAuthorName, String> {
    @Override
    public boolean isValid(String authorName, ConstraintValidatorContext context) {
        if (authorName == null) {
            return false;
        }

        String regex = "^[A-Z][a-zA-Z]* [A-Z][a-zA-Z]*$";
        return authorName.matches(regex);
    }
}
