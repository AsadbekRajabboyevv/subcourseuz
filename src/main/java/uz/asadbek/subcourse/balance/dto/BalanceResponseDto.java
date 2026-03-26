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
    private String userFullName;                // User ismi
    private Long balance;                       // Asosiy balans
    private Long pendingBalance;                // Kutilayotgan balans
    private Long totalBalance;                  // Umumiy balans (balance + pendingBalance)
    private CurrencyEnum currency;              // Pul birligi
    private LocalDateTime lastTransactionAt;    // Oxirgi tranzaksiya vaqti
    public BalanceResponseDto(
        Long userId,
        String userFullName,
        Long balance,
        Long pendingBalance,
        int totalBalance,
        CurrencyEnum currency,
        LocalDateTime lastTransactionAt) {

        this.userId = userId;
        this.userFullName = userFullName;
        this.balance = balance;
        this.pendingBalance = pendingBalance;
        this.totalBalance = (long) totalBalance;
        this.currency = currency;
        this.lastTransactionAt = lastTransactionAt;
    }
}
