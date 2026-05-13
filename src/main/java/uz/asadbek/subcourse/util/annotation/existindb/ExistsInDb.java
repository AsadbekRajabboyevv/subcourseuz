package uz.asadbek.subcourse.util.annotation.existindb;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import uz.asadbek.subcourse.util.annotation.Severity;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistInDbValidator.class)
public @interface ExistsInDb {

    String message() default "Resource not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> entity();

    boolean optional() default false;

    boolean setNull() default false;

    String field() default "id";
}
