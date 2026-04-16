package uz.asadbek.subcourse.util;

import lombok.experimental.UtilityClass;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.exception.ForbiddenException;
import uz.asadbek.subcourse.exception.InsufficientBalanceException;
import uz.asadbek.subcourse.exception.NotFoundException;
import uz.asadbek.subcourse.exception.PaymentException;
import uz.asadbek.subcourse.exception.UnAuthorizedException;

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
    public static PaymentException paymentException(String message) {
        return new PaymentException(message);
    }

    public static UnAuthorizedException unAuthorizedException(String message) {
        return new UnAuthorizedException(message);
    }
}
