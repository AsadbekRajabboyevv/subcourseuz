package uz.asadbek.subcourse.balance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptBalanceResponseDto {
    private String title;           // sarlavha
    private String message;         // xabar
    private Long balance;           // balans dagi summa
    private String transactionId;   // transzaksiya tashqi ID
    private TransactionStatus status; // tranzaksiya holati
}
