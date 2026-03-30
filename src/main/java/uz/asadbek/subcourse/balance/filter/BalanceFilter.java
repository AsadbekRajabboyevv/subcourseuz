package uz.asadbek.subcourse.balance.filter;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BalanceFilter {
    private String userFullName;
    private String userEmail;
    private LocalDateTime lastTransactionDateTo;
    private LocalDateTime lastTransactionDateFrom;

}
