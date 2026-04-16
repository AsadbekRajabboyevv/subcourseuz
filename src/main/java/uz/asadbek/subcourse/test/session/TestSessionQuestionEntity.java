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
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.asadbek.base.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "test_session_questions", indexes = {
    @Index(name = "idx_session_question", columnList = "session_id"),
    @Index(name = "idx_question_order", columnList = "session_id, order_index")
})
@EntityListeners(AuditingEntityListener.class)
public class TestSessionQuestionEntity extends BaseEntity {

    private static final String GEN = "test_session_questions_gen";
    private static final String SEQ = "test_session_questions_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GEN)
    @SequenceGenerator(name = GEN, sequenceName = SEQ, allocationSize = 50)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "question_text", columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "image_path")
    private String imagePath;
}
