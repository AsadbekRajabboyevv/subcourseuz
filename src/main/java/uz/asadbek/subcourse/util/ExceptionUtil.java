package uz.asadbek.subcourse.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.exception.NotFoundException;

@UtilityClass
public class ExceptionUtil {

    public static BadRequestException badRequestException(String message) {
        return new BadRequestException(message);
    }

    public static NotFoundException notFoundException(String message) {
        return new NotFoundException(message);
    }
}
