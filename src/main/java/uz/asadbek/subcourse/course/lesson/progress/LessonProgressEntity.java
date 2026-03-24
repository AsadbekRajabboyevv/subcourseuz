package uz.asadbek.subcourse.course.lesson.progress;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.asadbek.base.entity.BaseEntity;
import uz.asadbek.base.repository.SoftDeletable;
import uz.asadbek.subcourse.course.lesson.progress.dto.LessonProgressStatus;

@Getter
@Setter
@Entity
@Table(name = "lesson_progresses")
@EntityListeners(AuditingEntityListener.class)
public class LessonProgressEntity extends BaseEntity implements SoftDeletable {

    private static final String GENERATOR_NAME = "lesson_progresses_gen";
    private static final String SEQUENCE_NAME = "lesson_progresses_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 50)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "lesson_id", nullable = false)
    private Long lesson;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LessonProgressStatus status = LessonProgressStatus.NOT_STARTED;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
