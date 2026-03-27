package uz.asadbek.subcourse.balance.dto;

public enum CurrencyEnum {
    UZS("O'zbek so'mi"),
    USD("AQSh dollari"),
    RUB("Rossiya rubli");
    private final String displayName;

    CurrencyEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
