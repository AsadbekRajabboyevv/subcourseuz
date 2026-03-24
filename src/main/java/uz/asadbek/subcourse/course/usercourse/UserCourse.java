package uz.asadbek.subcourse.course.usercourse;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_courses")
public class UserCourse {

    @EmbeddedId
    private UserCourseId id;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}
