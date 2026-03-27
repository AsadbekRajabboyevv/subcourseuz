package uz.asadbek.subcourse.balance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelBalanceResponseDto {
    private String title;              // sarlavha
    private String message;            // xabar
    private Long balance;              // balansdagi summa
    private Long amount;               // foydalanuvchi tashlagan summa
    private TransactionStatus status;  // tranzaksiya holati
    private String transactionId;      // transzaksiya tashqi ID
}
