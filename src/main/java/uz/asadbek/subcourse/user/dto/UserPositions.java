package uz.asadbek.subcourse.user.dto;

public enum UserPositions {

    TALABA("Talaba"),
    OQUVCHI("O'quvchi"),
    OQITUVCHI("O'qituvchi"),
    OTA_ONA("Ota-ona"),
    BOSHQA("Boshqa");

    private final String displayName;

    UserPositions(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

