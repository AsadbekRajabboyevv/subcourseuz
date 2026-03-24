package uz.asadbek.subcourse.course.lesson;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.asadbek.base.entity.BaseEntity;
import uz.asadbek.base.repository.SoftDeletable;


@Getter
@Setter
@Entity
@Table(name = "course_lessons")
@EntityListeners(AuditingEntityListener.class)
public class CourseLessonEntity extends BaseEntity implements SoftDeletable {

    private static final String GENERATOR_NAME = "course_lessons_gen";
    private static final String SEQUENCE_NAME = "course_lessons_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 50)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lesson_number", nullable = false)
    private Integer lessonNumber;

    @Column(name = "text_content", columnDefinition = "text")
    private String textContent;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "course_id")
    private Long courseId;


}
