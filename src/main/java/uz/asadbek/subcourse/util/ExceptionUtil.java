package uz.asadbek.subcourse.util;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import uz.asadbek.subcourse.exception.BadRequestException;
import uz.asadbek.subcourse.exception.ValidationException;

@Slf4j
@Component
public class ExceptionUtil {

    private static MessageSource messageSource;
    private final MessageSource injectedMessageSource;

    public ExceptionUtil(MessageSource messageSource) {
        this.injectedMessageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        messageSource = this.injectedMessageSource;
    }

    /**
     * Generic Exception yaratuvchi metod
     * @param exceptionClass Qaysi exception tashlanishi kerakligi (masalan: NotFoundException.class)
     * @param key Properties faylidagi kalit
     * @param args Dinamik argumentlar ({0}, {1}...)
     */
    public static <T extends RuntimeException> T build(Class<T> exceptionClass, String key, Object... args) {
        try {
            String message = messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
            return exceptionClass.getConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            log.error("Exception yaratishda xatolik. Key: {}, Exception: {}", key, exceptionClass.getSimpleName(), e);
            throw new BadRequestException("INTERNAL_SERVER_ERROR");
        }
    }

    public static ValidationException validationException(
        String key, Map<String, String> fieldErrors, Object... args) {
        String message = messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        return new ValidationException(message, fieldErrors);
    }

    /**
     * Validatsiya xatosi — bitta field uchun.
     */
    public static ValidationException validationException(
        String field, String key, Object... args) {
        String message = messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        return new ValidationException(message, Map.of(field, message));
    }

    public static String resolveMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}
