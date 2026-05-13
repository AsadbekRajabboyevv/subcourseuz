package uz.asadbek.subcourse.test.session.option;

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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.asadbek.base.entity.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "test_session_options",
    indexes = {
        @Index(name = "idx_session_option_question", columnList = "session_question_id"),
        @Index(name = "idx_session_option_original", columnList = "original_option_id"),
        @Index(name = "idx_session", columnList = "session_id")
    }
)
@EntityListeners(AuditingEntityListener.class)
public class TestSessionOptionEntity extends BaseEntity {

    private static final String GEN = "test_session_options_gen";
    private static final String SEQ = "test_session_options_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GEN)
    @SequenceGenerator(name = GEN, sequenceName = SEQ, allocationSize = 50)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "session_question_id", nullable = false)
    private Long sessionQuestionId;

    @Column(name = "original_option_id")
    private Long originalOptionId;

    @Column(name = "option_text", columnDefinition = "TEXT")
    private String optionText;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "order_number")
    private Integer orderNumber;

    public TestSessionOptionEntity(
        Long sessionId,
        Long sessionQuestionId,
        Long originalOptionId,
        String text,
        String imagePath,
        Integer orderNumber
    ) {
        this.sessionId = sessionId;
        this.sessionQuestionId = sessionQuestionId;
        this.originalOptionId = originalOptionId;
        this.optionText = text;
        this.imagePath = imagePath;
        this.orderNumber = orderNumber;
    }
}
