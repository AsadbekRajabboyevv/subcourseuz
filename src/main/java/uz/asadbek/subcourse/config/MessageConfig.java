package uz.asadbek.subcourse.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class MessageConfig {

    @Bean
    public MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public AcceptHeaderLocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();

        return new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String headerLang = request.getHeader("Accept-Language");

                if (headerLang == null || headerLang.isEmpty()) {
                    return new Locale("uz", "cyrl");
                }

                if (headerLang.equalsIgnoreCase("uz-cyrl") || headerLang.equalsIgnoreCase("uz-crl")) {
                    return new Locale("uz", "cyrl");
                }

                if (headerLang.equalsIgnoreCase("uz")) {
                    return new Locale("uz");
                }

                if (headerLang.equalsIgnoreCase("ru")) {
                    return new Locale("ru");
                }

                if (headerLang.equalsIgnoreCase("en")) {
                    return new Locale("en");
                }

                return new Locale("uz", "cyrl");
            }
        };
    }
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }
}
