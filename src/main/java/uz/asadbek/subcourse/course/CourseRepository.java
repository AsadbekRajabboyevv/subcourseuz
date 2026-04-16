package uz.asadbek.subcourse.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;

@Repository
public interface CourseRepository extends BaseRepository<CourseEntity, Long> {

    @Query("""
            select new uz.asadbek.subcourse.course.dto.CourseResponseDto(
                c.id,
                c.name,
                count(distinct l.id),
                count(distinct sc.id.userId),
                concat(coalesce(u.firstName, ''), ' ', coalesce(u.lastName, '')),
                c.price,
                c.imagePath,
                c.lang
            )
            from CourseEntity c
            left join UserEntity u on c.ownerId = u.id
            left join CourseLessonEntity l on l.courseId = c.id
            left join UserCourseEntity sc
                on sc.id.referenceId = c.id
                and (:currentUserId is null or sc.id.userId = :currentUserId)
            where c.deletedAt is null
            and (:#{#filter.id} is null or c.id = :#{#filter.id})
            and (:#{#filter.createdAtFrom} is null or c.createdAt >= :#{#filter.createdAtFrom})
            and (:#{#filter.createdAtTo} is null or c.createdAt <= :#{#filter.createdAtTo})
            and (:#{#filter.updatedAtFrom} is null or c.updatedAt >= :#{#filter.updatedAtFrom})
            and (:#{#filter.updatedAtTo} is null or c.updatedAt <= :#{#filter.updatedAtTo})
            and (:#{#filter.gradeId} is null or c.gradeId = :#{#filter.gradeId})
            and (:#{#filter.scienceId} is null or c.scienceId = :#{#filter.scienceId})
            and (:#{#filter.priceFrom} is null or c.price >= :#{#filter.priceFrom})
            and (:#{#filter.priceTo} is null or c.price <= :#{#filter.priceTo})
            and (:#{#filter.lang} is null or c.lang = :#{#filter.lang})
            and (:#{#filter.search} is null or (
                   lower(c.name) like lower(concat('%', :#{#filter.search}, '%'))
                or lower(concat(coalesce(u.firstName, ''), ' ', coalesce(u.lastName, ''))) like lower(concat('%', :#{#filter.search}, '%'))
                or lower(c.description) like lower(concat('%', :#{#filter.search}, '%'))
            ))
            and (:#{#filter.duration} is null or :#{#filter.durationType} is null or
                ((case c.durationType
                      when 'SOAT' then c.duration
                      when 'KUN' then c.duration * 24
                      when 'OY' then c.duration * 30 * 24
                      when 'YIL' then c.duration * 365 * 24
                 end)
                 >=
                 (case :#{#filter.durationType?.name()}
                      when 'SOAT' then :#{#filter.duration}
                      when 'KUN' then :#{#filter.duration} * 24
                      when 'OY' then :#{#filter.duration} * 30 * 24
                      when 'YIL' then :#{#filter.duration} * 365 * 24
                 end)))
            group by
                c.id,
                c.name,
                u.firstName,
                u.lastName,
                c.price,
                c.imagePath,
                c.lang
        """)
    Page<CourseResponseDto> get(Pageable pageable,
        CourseFilter filter, String lang, Long currentUserId);

    @Query("""
            select new uz.asadbek.subcourse.course.dto.CourseResponseDto(
                c.id,
                c.name,
                count(distinct l.id),
                count(distinct sc.id),
                concat(coalesce(u.firstName, ''), ' ', coalesce(u.lastName, '')),
                c.price,
                c.imagePath,
                c.lang
            )
            from CourseEntity c
            left join UserEntity u on c.ownerId = u.id
            left join CourseLessonEntity l on l.courseId = c.id
            left join UserCourseEntity sc on sc.id.referenceId = c.id
            where c.deletedAt is null and c.id = :id
            group by c.id, c.name, u.firstName, u.lastName, c.price, c.imagePath, c.lang
        """)
    CourseResponseDto get(Long id);

    @Query(value = """
        SELECT
            c.id,
            c.name,
            c.description,

            CASE
                WHEN :lang = 'uz' THEN g.name_uz
                WHEN :lang = 'ru' THEN g.name_ru
                WHEN :lang = 'en' THEN g.name_en
                WHEN :lang = 'crl' THEN g.name_crl
                ELSE g.name_uz
            END AS grade_name,

            CASE
                WHEN :lang = 'uz' THEN s.name_uz
                WHEN :lang = 'ru' THEN s.name_ru
                WHEN :lang = 'en' THEN s.name_en
                WHEN :lang = 'crl' THEN s.name_crl
                ELSE s.name_uz
            END AS science_name,
            c.duration,
            c.duration_type,

            COUNT(DISTINCT l.id) AS lessons_count,
            COUNT(DISTINCT sc.user_id) AS students_count,

            CONCAT(COALESCE(u.first_name, ''), ' ', COALESCE(u.last_name, '')) AS owner_name,

            c.price,
            c.image_path,
            c.lang,
            (
                SELECT (:currentUserId IS NOT NULL) AND EXISTS (
                SELECT 1 FROM user_courses sc2
                WHERE sc2.reference_id = c.id
                  AND sc2.user_id = :currentUserId
            )) AS purchased,

            COALESCE(
                JSON_AGG(
                    JSON_BUILD_OBJECT(
                        'id', l.id,
                        'name', l.name,
                        'lessonNumber',lesson_number
                    )
                ) FILTER (WHERE l.id IS NOT NULL),
                '[]'
            ) AS lessons,
            c.science_id,
            c.grade_id
        FROM courses c
        LEFT JOIN users u ON c.owner_id = u.id
        LEFT JOIN course_lessons l ON l.course_id = c.id
        LEFT JOIN user_courses sc ON sc.reference_id = c.id
        LEFT JOIN course_grades g ON c.grade_id = g.id
        LEFT JOIN sciences s ON c.science_id = s.id

        WHERE c.deleted_at IS NULL
          AND c.id = :id

        GROUP BY c.id, g.id, s.id, u.id
        """, nativeQuery = true)
    Object[] get(Long id, String lang, Long currentUserId);

    boolean existsByIdAndScienceId(Long id, Long scienceId);
}
