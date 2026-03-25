package uz.asadbek.subcourse.balance.dto;

public enum TransactionType {
    TOP_UP("Balans to'ldirish"),
    COURSE_PURCHASE("Kurs sotib olish"),
    TEST_PURCHASE("Kurs sotib olish"),
    REFUND("To'lov qaytarish"),
    ADMIN_ADJUSTMENT("Admin tomonidan tuzatish");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
