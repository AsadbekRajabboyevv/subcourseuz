package uz.asadbek.subcourse.course.lesson;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonInfoResponseDto;
import uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto;

@Repository
public interface CourseLessonRepository extends BaseRepository<CourseLessonEntity, Long> {

    @Query("""
          select new uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto(
               cl.id,
               cl.name,
               cl.lessonNumber,
               cl.isPublished
          )
          from CourseLessonEntity cl
          where cl.courseId = :#{#courseId}
          and cl.deletedAt is null
    """)
    List<CourseLessonResponseDto> getByCourseId(Long courseId);

    @Query("""
          select new uz.asadbek.subcourse.course.lesson.dto.CourseLessonInfoResponseDto(
               cl.id,
               cl.name,
               cl.lessonNumber,
               cl.videoUrl,
               c.name,
               c.imagePath,
               cl.textContent,
               c.id,
               cl.isPublished,
               cl.fileUrls
          )
          from CourseLessonEntity cl
          left join CourseEntity c on cl.courseId = c.id
          where cl.id = :#{#id}
          and cl.deletedAt is null

    """)
    CourseLessonInfoResponseDto get(Long id);

    boolean existsByIdAndCourseId(Long lessonId, Long courseId);

    Long countCourseLessonEntitiesByDeletedAtIsNullAndVideoUrlIsNotNullAndIsPublishedIsTrue();

    @Query("""
        SELECT new uz.asadbek.subcourse.course.lesson.dto.CourseLessonResponseDto(
            l.id,
            l.name,
            l.lessonNumber,
            l.isPublished
        )
        FROM CourseLessonEntity l
        WHERE l.courseId = :courseId
        AND (:isPublished IS NULL OR l.isPublished = :isPublished)
        ORDER BY l.lessonNumber ASC
    """)
    List<CourseLessonResponseDto> findAllByCourseId(Long courseId, Boolean isPublished);
}
