package uz.asadbek.subcourse.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.asadbek.subcourse.auth.CustomUserDetails;
import uz.asadbek.subcourse.util.embedded.DescriptionEmbedded;
import uz.asadbek.subcourse.util.embedded.NameEmbedded;

public final class LangUtils {

    private LangUtils() {}

    public static final String DEFAULT_LANG = "uz";

    public static String currentLang() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails user)) {
            return DEFAULT_LANG;
        }

        String lang = user.getLanguage();
        return normalize(lang);
    }

    public static String getName(NameEmbedded name) {
        if (name == null) return null;

        String lang = currentLang();

        String resolved = switch (lang) {
            case "ru" -> name.getNameRu();
            case "en" -> name.getNameEn();
            case "crl" -> name.getNameCrl();
            default -> name.getNameUz();
        };

        return fallback(resolved, name.getNameUz());
    }

    public static String getDescription(DescriptionEmbedded desc) {
        if (desc == null) return null;

        String lang = currentLang();

        String resolved = switch (lang) {
            case "ru" -> desc.getDescriptionRu();
            case "en" -> desc.getDescriptionEn();
            case "crl" -> desc.getDescriptionCrl();
            default -> desc.getDescriptionUz();
        };

        return fallback(resolved, desc.getDescriptionUz());
    }

    private static String normalize(String lang) {
        if (lang == null || lang.isBlank()) return DEFAULT_LANG;

        lang = lang.toLowerCase();

        if (lang.contains("-")) {
            return lang.split("-")[0];
        }

        return lang;
    }

    private static String fallback(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }
}
