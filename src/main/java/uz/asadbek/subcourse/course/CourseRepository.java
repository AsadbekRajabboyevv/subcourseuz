package uz.asadbek.subcourse.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;
import uz.asadbek.subcourse.course.dto.CourseInfoResponseDto;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.course.filter.CourseFilter;

@Repository
public interface CourseRepository extends BaseRepository<CourseEntity, Long> {

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
            left join UserCourse sc on sc.id.courseId = c.id
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
            and (:#{#filter.name} is null or lower(c.name) like lower(concat('%', :#{#filter.name}, '%')))
            and (:#{#filter.ownerName} is null or lower(concat(coalesce(u.firstName, ''), ' ', coalesce(u.lastName, ''))) like lower(concat('%', :#{#filter.ownerName}, '%')))

            and (:#{#filter.duration} is null or :#{#filter.durationType} is null or
                ((case c.durationType
                      when 'SOAT' then c.duration
                      when 'KUN' then c.duration * 24
                      when 'OY' then c.duration * 24 * 30
                      when 'YIL' then c.duration * 24 * 365
                 end)
                 >=
                 (case :#{#filter.durationType.name()}
                      when 'SOAT' then :#{#filter.duration}
                      when 'KUN' then :#{#filter.duration} * 24
                      when 'OY' then :#{#filter.duration} * 24 * 30
                      when 'YIL' then :#{#filter.duration} * 24 * 365
                 end)))
        """)
    Page<CourseResponseDto> get(Pageable pageable,
        CourseFilter filter, String lang);

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
            left join UserCourse sc on sc.id.courseId = c.id
            where c.deletedAt is null and c.id = :#{#id}
        """)
    CourseResponseDto get(Long id);

    @Query("""
            select new uz.asadbek.subcourse.course.dto.CourseInfoResponseDto(
                c.id,
                c.name,
                c.description,
                case
                    when :lang = 'uz' then g.name.nameUz
                    when :lang = 'ru' then g.name.nameRu
                    when :lang = 'en' then g.name.nameEn
                    when :lang = 'crl' then g.name.nameCrl
                    else g.name.nameUz
                end,
                case
                    when :lang = 'uz' then s.name.nameUz
                    when :lang = 'ru' then s.name.nameRu
                    when :lang = 'en' then s.name.nameEn
                    when :lang = 'crl' then s.name.nameCrl
                    else s.name.nameUz
                end,
                c.duration,
                c.durationType,
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
            left join UserCourse sc on sc.id.courseId = c.id
            left join CourseGradeEntity g on c.gradeId = g.id
            left join ScienceEntity s on c.scienceId = s.id
            where c.deletedAt is null
              and c.id = :id
        """)
    CourseInfoResponseDto get(Long id, String lang);
}
