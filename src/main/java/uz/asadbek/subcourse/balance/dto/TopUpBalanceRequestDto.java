package uz.asadbek.subcourse.balance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopUpBalanceRequestDto {
    private Long amount;
    private String message;
}
