package uz.asadbek.subcourse.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private PaymentStatus status;
    private String transactionId;
    private Long amount;
    private CurrencyEnum currency;
    private PaymentType type;
    @JsonIgnore
    private Long paymentId;
}
