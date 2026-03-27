package uz.asadbek.subcourse.balance.topuprequest.dto;

public enum TopUpStatus {
    CREATED,     // user yubordi
    PENDING,     // ko‘rib chiqilmoqda
    APPROVED,    // admin tasdiqladi
    REJECTED,    // rad etildi
    CANCELLED
}
