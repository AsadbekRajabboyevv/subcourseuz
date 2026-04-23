package uz.asadbek.subcourse.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.asadbek.base.entity.BaseEntity;
import uz.asadbek.base.repository.SoftDeletable;

@Getter
@Setter
@Entity
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class CommentEntity extends BaseEntity implements SoftDeletable {

    private static final String GENERATOR_NAME = "comments_gen";
    private static final String SEQUENCE_NAME  = "comments_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 50)
    private Long id;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "lesson_id")
    private Long lessonId;

    @Column(name = "test_id")
    private Long testId;

    @Min(1) @Max(5)
    @Column(name = "rating")
    private Integer rating;
}
