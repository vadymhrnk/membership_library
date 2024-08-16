package com.example.membershiplibrary.annotation.book;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AuthorNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAuthorName {
    String message() default "should contain two capital words "
            + "with name and surname and space between";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
