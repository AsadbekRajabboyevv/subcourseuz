package uz.asadbek.subcourse.balance.transaction;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import uz.asadbek.subcourse.balance.dto.TransactionStatus;
import uz.asadbek.subcourse.balance.dto.TransactionType;

@Entity
@Table(name = "balance_transactions")
@Getter
@Setter
public class BalanceTransactionEntity {

    private static final String GENERATOR_NAME = "balance_transactions_gen";
    private static final String SEQUENCE_NAME = "balance_transactions_seq";

    @Id
    @Column(name = "id")
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = GENERATOR_NAME
    )
    @SequenceGenerator(
        name = GENERATOR_NAME,
        sequenceName = SEQUENCE_NAME,
        allocationSize = 1
    )
    private Long id;                                            // balance transaction id

    @Column(
        name = "external_tx",
        unique = true,
        nullable = false
    )
    private String externalTx;                                  // tashqi id

    @Column(name = "user_id", nullable = false)
    private Long userId;                                        // user id

    @Column(name = "payment_id")
    private Long paymentId;                                     // balance id

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;                    // tranzaksiya turi

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;                           // tranzaksiya holati

    @Column(name = "amount", nullable = false)
    private Long amount;                                        // miqdor

    @Column(name = "balance_before", nullable = false)
    private Long balanceBefore;                                 // hozirgi balansdan oldin

    @Column(name = "balance_after", nullable = false)
    private Long balanceAfter;                                  // hozirgi balansdan keyin

    @Column(name = "description")
    private String description;                                 // izoh

    @Column(name = "reference_id")
    private Long referenceId;                                   // COURSE_ID yoki TEST_ID

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;
}
