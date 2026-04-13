package uz.asadbek.subcourse.course.usercourse;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.asadbek.subcourse.util.embedded.UserPurchaseId;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_courses")
public class UserCourseEntity {

    @EmbeddedId
    private UserPurchaseId id;

}
