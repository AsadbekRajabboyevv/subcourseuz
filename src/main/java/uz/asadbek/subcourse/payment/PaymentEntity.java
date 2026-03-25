package uz.asadbek.subcourse.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.asadbek.base.entity.BaseEntity;
import uz.asadbek.subcourse.payment.dto.PaymentStatus;
import uz.asadbek.subcourse.payment.dto.PaymentType;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class PaymentEntity extends BaseEntity {

    private static final String GENERATOR_NAME = "payments_gen";
    private static final String SEQUENCE_NAME = "payments_seq";

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = GENERATOR_NAME
    )
    @SequenceGenerator(
        name = GENERATOR_NAME,
        sequenceName = SEQUENCE_NAME,
        allocationSize = 50
    )
    private Long id;

    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "type", nullable = false)
    private PaymentType type;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "ex_id", nullable = false)
    private String exId;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
