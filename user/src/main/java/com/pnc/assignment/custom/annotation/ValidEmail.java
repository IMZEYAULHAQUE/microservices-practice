package com.pnc.assignment.custom.annotation;

import com.pnc.assignment.custom.annotation.implementation.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
@Email
public @interface ValidEmail {

   String message() default "Invalid email";
   Class<?>[] groups() default {};
   Class<? extends Payload>[] payload() default {};

}
