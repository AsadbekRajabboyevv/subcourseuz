package uz.asadbek.subcourse.balance.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopUpBalanceResponseDto {
    private String transactionId;      // transzaksiya tashqi ID
    private TransactionStatus status;  // tranzaksiya holati
    private Long amount;               // tashlangan pul miqdori
    private String message;            // System message
    private LocalDateTime expiresAt;   // Ko'rib chiqish vaqti
}
