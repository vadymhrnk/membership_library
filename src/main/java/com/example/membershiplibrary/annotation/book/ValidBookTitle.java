package com.example.membershiplibrary.annotation.book;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BookTitleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookTitle {
    String message() default "should start with a capital letter, min length - 3 symbols";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
