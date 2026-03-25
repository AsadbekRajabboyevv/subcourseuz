package uz.asadbek.subcourse.balance.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponseDto {
    private Long userId;                        // User ID
    private Long balance;                       // Asosiy balans
    private Long holdBalance;                   // Bloklangan balans
    private Long pendingBalance;                // Kutilayotgan balans
    private Long availableBalance;              // Mavjud balans (balance - holdBalance)
    private Long totalBalance;                  // Umumiy balans (balance + pendingBalance)
    private CurrencyEnum currency;              // Pul birligi
    private LocalDateTime lastTransactionAt;    // Oxirgi tranzaksiya vaqti
}
