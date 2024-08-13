package com.example.membershiplibrary.annotation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookTitleValidator implements ConstraintValidator<ValidBookTitle, String> {
    @Override
    public boolean isValid(String bookName, ConstraintValidatorContext context) {
        if (bookName == null || bookName.length() < 3) {
            return false;
        }

        return Character.isUpperCase(bookName.charAt(0));
    }
}
