package uz.asadbek.subcourse.util;

import lombok.experimental.UtilityClass;

import java.text.Normalizer;

@UtilityClass
public class SlugUtil {

    public static String generateSlug(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }

        String slug = text.toLowerCase();
        slug = transliterateCyrillicToLatin(slug);
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");
        slug = slug.replaceAll("\\s+", "-");
        slug = slug.replaceAll("-{2,}", "-");
        slug = slug.replaceAll("^-|-$", "");
        if (slug.length() > 100) {
            slug = slug.substring(0, 100);
        }

        return slug;
    }

    private static String transliterateCyrillicToLatin(String input) {
        return input
            .replace("а", "a")
            .replace("б", "b")
            .replace("в", "v")
            .replace("г", "g")
            .replace("д", "d")
            .replace("е", "e")
            .replace("ё", "e")
            .replace("ж", "zh")
            .replace("з", "z")
            .replace("и", "i")
            .replace("й", "y")
            .replace("к", "k")
            .replace("л", "l")
            .replace("м", "m")
            .replace("н", "n")
            .replace("о", "o")
            .replace("п", "p")
            .replace("р", "r")
            .replace("с", "s")
            .replace("т", "t")
            .replace("у", "u")
            .replace("ф", "f")
            .replace("х", "h")
            .replace("ц", "ts")
            .replace("ч", "ch")
            .replace("ш", "sh")
            .replace("щ", "sh")
            .replace("ъ", "")
            .replace("ы", "y")
            .replace("ь", "")
            .replace("э", "e")
            .replace("ю", "yu")
            .replace("я", "ya");
    }
}
