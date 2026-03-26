package uz.asadbek.subcourse.balance.topuprequest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopUpBalanceRequestDto {
    private Long amount;
    private String message;
}
