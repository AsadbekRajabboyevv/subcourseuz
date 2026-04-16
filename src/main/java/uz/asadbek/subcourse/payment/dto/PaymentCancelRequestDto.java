package uz.asadbek.subcourse.payment.dto;

import lombok.Getter;

@Getter
public class PaymentCancelRequestDto {
    private String exId;
    private String cancelReason;
}
