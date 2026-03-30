package uz.asadbek.subcourse.test.session;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.asadbek.base.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "test_session_answers", indexes = {
    @Index(name = "idx_answer_session", columnList = "session_id"),
    @Index(name = "idx_answer_unique", columnList = "session_id, question_id", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class TestSessionAnswerEntity extends BaseEntity {

    private static final String GEN = "test_session_answers_gen";
    private static final String SEQ = "test_session_answers_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GEN)
    @SequenceGenerator(name = GEN, sequenceName = SEQ, allocationSize = 50)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "selected_option_id")
    private Long selectedOptionId;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;
}
