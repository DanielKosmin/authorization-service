package com.kosmin.authorization.validator;

import jakarta.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegistrationValidator.class)
public @interface ValidateRegistration {
  String message() default "Invalid user registration";

  Class<?>[] groups() default {};

  Class<?>[] payload() default {};
}
