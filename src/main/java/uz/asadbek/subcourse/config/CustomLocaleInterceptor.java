package uz.asadbek.subcourse.config;

import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

public class CustomLocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String headerLang = request.getHeader("Accept-Language");
        Locale locale;

        if (headerLang == null || headerLang.isEmpty()) {
            locale = new Locale("uz");
        } else if (headerLang.equalsIgnoreCase("uz-Cyrl")) {
            locale = new Locale("uz-Cyrl", "UZ");
        } else {
            locale = Locale.forLanguageTag(headerLang.split(",")[0]);
        }

        LocaleContextHolder.setLocale(locale);
        return true;
    }
}
