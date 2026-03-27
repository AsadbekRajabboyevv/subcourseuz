package uz.asadbek.subcourse.balance.topuprequest.dto;

import lombok.Getter;

@Getter
public class TopUpRequestActionRequestDto {
    private Long id;
    private String message;
    private String paymentExId;
}
