package uz.asadbek.subcourse.course;

import jakarta.persistence.Column;
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
import uz.asadbek.subcourse.course.dto.DurationType;
import uz.asadbek.subcourse.util.embedded.DescriptionEmbedded;
import uz.asadbek.subcourse.util.embedded.NameEmbedded;

@Getter
@Setter
@Entity
@Table(name = "courses")
@EntityListeners(AuditingEntityListener.class)
public class CourseEntity extends BaseEntity implements SoftDeletable {

    private static final String GENERATOR_NAME = "courses_gen";
    private static final String SEQUENCE_NAME = "courses_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR_NAME)
    @SequenceGenerator(name = GENERATOR_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 50)
    private Long id;

    @Column(name = "price")
    private Long price;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "duration_type")
    private DurationType durationType;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_published", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isPublished;

    @Column(name = "science_id")
    private Long scienceId;

    @Column(name = "grade_id")
    private Long gradeId;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "lang")
    private String lang;

    @Column(name = "is_video_course", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isVideoCourse;
}
