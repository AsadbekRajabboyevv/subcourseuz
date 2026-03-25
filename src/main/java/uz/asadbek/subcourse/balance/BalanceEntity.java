package uz.asadbek.subcourse.balance;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import uz.asadbek.base.entity.BaseEntity;
import uz.asadbek.subcourse.balance.dto.CurrencyEnum;

@Getter
@Setter
@Entity
@Table(name = "balances")
public class BalanceEntity extends BaseEntity {

    private static final String GENERATOR_NAME = "balances_gen";
    private static final String SEQUENCE_NAME = "balances_seq";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private Long userId;
    @Column(name = "balance", nullable = false, columnDefinition = "DEFAULT SET 0")
    private Long balance;

    @Column(name = "hold_balance", nullable = false, columnDefinition = "DEFAULT SET 0")
    private Long holdBalance;

    @Column(name = "pending_balance", nullable = false, columnDefinition = "DEFAULT SET 0")
    private Long pendingBalance;

    @Column(name = "last_transaction_at")
    private LocalDateTime lastTransactionAt;

    @Column(name = "currency", nullable = false)
    private CurrencyEnum currency;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @Version
    @Column(name = "version")
    private Long version;

}
