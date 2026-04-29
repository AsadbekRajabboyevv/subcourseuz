package uz.asadbek.subcourse.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.asadbek.subcourse.exception.*;
import org.springframework.security.core.AuthenticationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "UN_AUTHORIZED", ExceptionUtil.resolveMessage("error.auth.invalid_token"));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        JwtUtil.getCurrentUserRoles().forEach(role -> log.info("Role: {}", role));
        return buildResponse(HttpStatus.FORBIDDEN, "FORBIDDEN", ExceptionUtil.resolveMessage("error.auth.insufficient_privileges"));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "FORBIDDEN", ex.getMessage());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthorized(ForbiddenException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "UN_AUTHORIZED", ex.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleBalance(InsufficientBalanceException ex) {
        return buildResponse(HttpStatus.PAYMENT_REQUIRED, "INSUFFICIENT_BALANCE", ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePayment(PaymentException ex) {
        return buildResponse(HttpStatus.PAYMENT_REQUIRED, "PAYMENT_ERROR", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(){
        return buildResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "BAD REQUEST");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse response = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode("VALIDATION_FAILED")
            .errorMessage(ExceptionUtil.resolveMessage("error.validation"))
            .fieldErrorMessage(errors)
            .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        var response = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode("VALIDATION_FAILED")
            .errorMessage(ExceptionUtil.resolveMessage("error.validation"))
            .fieldErrorMessage(ex.getFieldErrors())
            .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
        HttpStatus status,
        String code,
        String message
    ) {
        ErrorResponse response = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .errorCode(code)
            .errorMessage(message)
            .build();

        return ResponseEntity
            .status(status)
            .body(response);
    }
}
