package uz.asadbek.subcourse.comment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = HasTargetValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasTarget {
    String message() default "courseId, lessonId yoki testId dan kamida bittasi ko'rsatilishi shart";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
