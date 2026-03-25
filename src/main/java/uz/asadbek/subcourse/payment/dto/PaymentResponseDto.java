package uz.asadbek.subcourse.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private String exId;
    private String status;
    private String transactionId;
    private Long amount;
}
