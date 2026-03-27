package uz.asadbek.subcourse.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.asadbek.subcourse.balance.dto.CurrencyEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private String exId;
    private String status;
    private String transactionId;
    private Long amount;
    private CurrencyEnum currency;
}
