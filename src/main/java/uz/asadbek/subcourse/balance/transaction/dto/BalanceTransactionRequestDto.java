package uz.asadbek.subcourse.balance.transaction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import uz.asadbek.subcourse.balance.dto.TransactionType;

@Data
@Builder
public class BalanceTransactionRequestDto {

    /**
     * Qaysi user uchun
     */
    @NotNull
    private Long userId;

    /**
     * Payment bilan bog‘lash uchun (optional emas deyarli)
     */
    private Long paymentId;

    /**
     * Tranzaksiya turi (TOP_UP, PURCHASE, ...)
     */
    @NotNull
    private TransactionType transactionType;

    /**
     * Summasi (faqat musbat qiymat)
     */
    @NotNull
    @Positive
    private Long amount;

    /**
     * Izoh (ixtiyoriy)
     */
    private String description;

    /**
     * Nima uchun to‘lov (courseId, testId ...)
     */
    private Long referenceId;

}
