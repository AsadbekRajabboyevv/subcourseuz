package uz.asadbek.subcourse.course.dto;

import java.util.Map;

public enum DurationType {
    OY("Oy", "Месяц", "Month", "Ой"),
    YIL("Yil", "Год", "Year", "Йил"),
    KUN("Kun", "День", "Day", "Кун"),
    SOAT("Soat", "Час", "Hour", "Соат");

    private final String uz;
    private final String ru;
    private final String en;
    private final String crl;

    DurationType(String uz, String ru, String en, String crl) {
        this.uz = uz;
        this.ru = ru;
        this.en = en;
        this.crl = crl;
    }

    public Map<String, String> getValues() {
        return Map.of(
            "uz", this.uz,
            "ru", this.ru,
            "en", this.en,
            "crl", this.crl
        );
    }
}
