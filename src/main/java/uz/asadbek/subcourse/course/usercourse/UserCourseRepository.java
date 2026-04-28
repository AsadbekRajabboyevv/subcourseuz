package uz.asadbek.subcourse.course.usercourse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.util.embedded.UserPurchaseId;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourseEntity, UserPurchaseId> {

    Long countByIdReferenceId(Long id);

    boolean existsByIdUserIdAndIdReferenceId(Long currentUserId, Long id);
}
