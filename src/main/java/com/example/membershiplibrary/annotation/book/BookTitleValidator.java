package com.example.membershiplibrary.annotation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookTitleValidator implements ConstraintValidator<ValidBookTitle, String> {
    public static final int FIRST_CHARACTER_INDEX = 0;
    public static final int MINIMAL_TITLE_LENGTH = 3;

    @Override
    public boolean isValid(String bookName, ConstraintValidatorContext context) {
        if (bookName == null || bookName.length() < MINIMAL_TITLE_LENGTH) {
            return false;
        }

        return Character.isUpperCase(bookName.charAt(FIRST_CHARACTER_INDEX));
    }
}
