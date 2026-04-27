package uz.asadbek.subcourse.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.asadbek.subcourse.auth.CustomUserDetails;
import uz.asadbek.subcourse.util.embedded.DescriptionEmbedded;
import uz.asadbek.subcourse.util.embedded.NameEmbedded;

public final class LangUtils {

    public static final String DEFAULT_LANG = "uz";
    private static final Map<String, String> MAP = new LinkedHashMap<>();
    private static final String LANG_HEADER = "Accept-Language";
    static {
        MAP.put("sh", "ш");
        MAP.put("ch", "ч");
        MAP.put("ng", "нг");
        MAP.put("g'", "ғ");
        MAP.put("o'", "ў");
        MAP.put("Sh", "Ш");
        MAP.put("Ch", "Ч");
        MAP.put("Ng", "Нг");
        MAP.put("G'", "Ғ");
        MAP.put("O'", "Ў");
        MAP.put("a", "а");
        MAP.put("b", "б");
        MAP.put("d", "д");
        MAP.put("e", "е");
        MAP.put("f", "ф");
        MAP.put("g", "г");
        MAP.put("h", "ҳ");
        MAP.put("i", "и");
        MAP.put("j", "ж");
        MAP.put("k", "к");
        MAP.put("l", "л");
        MAP.put("m", "м");
        MAP.put("n", "н");
        MAP.put("o", "о");
        MAP.put("p", "п");
        MAP.put("q", "қ");
        MAP.put("r", "р");
        MAP.put("s", "с");
        MAP.put("t", "т");
        MAP.put("u", "у");
        MAP.put("v", "в");
        MAP.put("x", "х");
        MAP.put("y", "й");
        MAP.put("z", "з");
        MAP.put("A", "А");
        MAP.put("B", "Б");
        MAP.put("D", "Д");
        MAP.put("E", "Е");
        MAP.put("F", "Ф");
        MAP.put("G", "Г");
        MAP.put("H", "Ҳ");
        MAP.put("I", "И");
        MAP.put("J", "Ж");
        MAP.put("K", "К");
        MAP.put("L", "Л");
        MAP.put("M", "М");
        MAP.put("N", "Н");
        MAP.put("O", "О");
        MAP.put("P", "П");
        MAP.put("Q", "Қ");
        MAP.put("R", "Р");
        MAP.put("S", "С");
        MAP.put("T", "Т");
        MAP.put("U", "У");
        MAP.put("V", "В");
        MAP.put("X", "Х");
        MAP.put("Y", "Й");
        MAP.put("Z", "З");
    }

    private LangUtils() {
    }

    public static String currentLang() {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return normalize(lang);
    }

    public static String getName(NameEmbedded name) {
        if (name == null) {
            return null;
        }

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
        if (desc == null) {
            return null;
        }

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
        if (lang == null || lang.isBlank()) {
            return DEFAULT_LANG;
        }

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

    public static String toCyrillic(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String result = text;

        for (Map.Entry<String, String> entry : MAP.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
