package uz.asadbek.subcourse.payment.dto;

import lombok.Getter;

@Getter
public class PaymentRequestDto {
    private Long courseId;
    private Long testId;
    private String couponCode;
}
