package uz.asadbek.subcourse.course.grade;

import jakarta.persistence.Embedded;
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
import uz.asadbek.subcourse.util.embedded.DescriptionEmbedded;
import uz.asadbek.subcourse.util.embedded.NameEmbedded;

@Getter
@Setter
@Entity
@Table(name = "course_grades")
@EntityListeners(AuditingEntityListener.class)
public class CourseGradeEntity extends BaseEntity implements SoftDeletable {

    private static final String GENERATOR_NAME = "course_grades_gen";
    private static final String SEQUENCE_NAME = "course_grades_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 50)
    private Long id;

    @Embedded
    private NameEmbedded name;

    @Embedded
    private DescriptionEmbedded description;

}
