package com.example.membershiplibrary.annotation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthorNameValidator implements ConstraintValidator<ValidAuthorName, String> {

    public static final String AUTHOR_NAME_REGEX = "^[A-Z][a-zA-Z]* [A-Z][a-zA-Z]*$";

    @Override
    public boolean isValid(String authorName, ConstraintValidatorContext context) {
        if (authorName == null) {
            return false;
        }
        
        return authorName.matches(AUTHOR_NAME_REGEX);
    }
}
