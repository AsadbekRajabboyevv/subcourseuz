package uz.asadbek.subcourse.user.dto;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;
import uz.asadbek.subcourse.user.UserService;

@Target({FIELD, METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
    validatedBy = UserEmailUnique.UserEmailUniqueValidator.class
)
public @interface UserEmailUnique {

    String message() default "email_already_exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UserEmailUniqueValidator implements ConstraintValidator<UserEmailUnique, String> {

        private final UserService userService;
        private final HttpServletRequest request;

        public UserEmailUniqueValidator(final UserService userService,
            final HttpServletRequest request) {
            this.userService = userService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                ((Map<String, String>) request.getAttribute(
                    HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final var currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(
                userService.get(Long.parseLong(currentId)).getEmail())) {
                return true;
            }
            return !userService.emailExists(value);
        }

    }

}
