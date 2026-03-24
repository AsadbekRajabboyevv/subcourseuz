package uz.asadbek.subcourse.course.usercourse;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepository
    extends JpaRepository<UserCourse, UserCourseId> {

    boolean existsByIdUserIdAndIdCourseId(Long userId, Long courseId);

    List<UserCourse> findByIdUserId(Long userId);

    List<UserCourse> findByIdCourseId(Long courseId);

    void deleteByIdUserIdAndIdCourseId(Long userId, Long courseId);

    long countByIdCourseId(Long courseId);

    long countByIdUserId(Long userId);

    @Modifying
    @Query("""
            delete from UserCourse uc
            where uc.id.courseId = :courseId
        """)
    void deleteAllByCourseId(@Param("courseId") Long courseId);
}
