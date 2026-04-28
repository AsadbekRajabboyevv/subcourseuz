package uz.asadbek.subcourse.course;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.dto.CourseUpdateRequestDto;
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
                c.lang,
                c.isPublished,
                c.slug
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
            and (:#{#filter.isPublished} is null or c.isPublished = :#{#filter.isPublished})
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
                c.lang,
                c.isPublished,
                c.slug
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
                c.lang,
                c.isPublished,
                c.slug
            )
            from CourseEntity c
            left join UserEntity u on c.ownerId = u.id
            left join CourseLessonEntity l on l.courseId = c.id
            left join UserCourseEntity sc on sc.id.referenceId = c.id
            where c.slug = :slug and c.deletedAt is null
            group by c.id, c.name, u.firstName, u.lastName, c.price, c.imagePath, c.lang
        """)
    CourseResponseDto get(String slug);

    @Query("""
            SELECT new uz.asadbek.subcourse.course.dto.CourseInfoResponseDto(
              c.id,
              c.name,
              c.description,
              CASE
                WHEN :lang = 'uz' THEN g.name.nameUz
                WHEN :lang = 'ru' THEN g.name.nameRu
                ELSE g.name.nameUz
              END,
              CASE
                WHEN :lang = 'uz' THEN s.name.nameUz
                WHEN :lang = 'ru' THEN s.name.nameRu
                ELSE s.name.nameUz
              END,
              c.duration,
              c.durationType,
              CONCAT(COALESCE(u.firstName, ''), ' ', COALESCE(u.lastName, '')),
              c.price,
              c.imagePath,
              c.lang,
              c.scienceId,
              c.gradeId,
              c.slug
            )
            FROM CourseEntity c
            LEFT JOIN CourseGradeEntity g ON c.gradeId = g.id
            LEFT JOIN ScienceEntity s ON c.scienceId = s.id
            LEFT JOIN UserEntity u ON c.ownerId = u.id
            WHERE c.slug = :slug AND c.deletedAt IS NULL
        """)
    Optional<CourseInfoResponseDto> getCourseBasicInfo(String slug, String lang);

    boolean existsByIdAndScienceId(Long id, Long scienceId);

    @Query("""
            SELECT new uz.asadbek.subcourse.course.dto.CourseUpdateRequestDto(
              c.name,
              c.description,
              c.duration,
              c.durationType,
              c.scienceId,
              c.gradeId,
              c.price,
              c.lang,
              c.isPublished,
              c.isVideoCourse,
              c.imagePath
            )
            FROM CourseEntity c
            WHERE c.deletedAt is null and c.slug = :slug
        """)
    Optional<CourseUpdateRequestDto> getUpdateData(String slug);

    boolean existsBySlug(String slug);

    @Query("SELECT id FROM CourseEntity where slug = :slug")
    Optional<Long> findIdBySlug(String slug);

    Optional<CourseEntity> findBySlug(String slug);
}
