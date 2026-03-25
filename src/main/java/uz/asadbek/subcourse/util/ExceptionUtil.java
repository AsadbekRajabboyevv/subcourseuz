package uz.asadbek.subcourse.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.exception.ForbiddenException;
import uz.asadbek.subcourse.exception.InsufficientBalanceException;
import uz.asadbek.subcourse.exception.NotFoundException;

@UtilityClass
public class ExceptionUtil {

    public static BadRequestException badRequestException(String message) {
        return new BadRequestException(message);
    }

    public static NotFoundException notFoundException(String message) {
        return new NotFoundException(message);
    }

    public static ForbiddenException forbiddenException(String message) {
        return new ForbiddenException(message);
    }
    public static InsufficientBalanceException insufficientBalanceException(String message) {
        return new InsufficientBalanceException(message);
    }

}
