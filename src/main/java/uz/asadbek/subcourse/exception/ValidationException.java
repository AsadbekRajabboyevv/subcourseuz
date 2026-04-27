package uz.asadbek.subcourse.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.fieldErrors = Map.of();
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors != null ? Map.copyOf(fieldErrors) : Map.of();
    }

    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }
}
