package uz.asadbek.subcourse.balance.topuprequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.asadbek.base.entity.BaseEntity;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "top_up_requests", indexes = {
    @Index(name = "idx_top_up_user", columnList = "user_id"),
    @Index(name = "idx_top_up_status", columnList = "status")
})
public class TopUpRequestEntity extends BaseEntity {

    private static final String GENERATOR_NAME = "top_up_req_gen";
    private static final String SEQUENCE_NAME = "top_up_req_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 50)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "file_key")
    private String fileKey; // чек / screenshot (FileStorage bilan bog‘lanadi)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TopUpStatus status;

    @Column(name = "message")
    private String message; // user izohi pending uchun

    @Column(name = "comment")
    private String comment; // admin izohi (reject sababi)

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "payment_id")
    private Long paymentId; // approve bo‘lganda PaymentEntity ga link

}
