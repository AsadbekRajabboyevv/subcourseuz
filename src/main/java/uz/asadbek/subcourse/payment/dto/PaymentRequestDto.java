package uz.asadbek.subcourse.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {
    private Long courseId;
    private Long testId;
    private Long amount;
    private String couponCode;
}
