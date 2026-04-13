package uz.asadbek.subcourse.course.grade;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.base.repository.BaseRepository;
import uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto;
import uz.asadbek.subcourse.course.grade.dto.OneCourseGradeResponseDto;

@Repository
public interface CourseGradeRepository extends BaseRepository<CourseGradeEntity, Long> {

    @Query("""
            select new uz.asadbek.subcourse.course.grade.dto.CourseGradeResponseDto(
                cg.id,
                case
                    when :lang = 'uz' then cg.name.nameUz
                    when :lang = 'ru' then cg.name.nameRu
                    when :lang = 'en' then cg.name.nameEn
                    when :lang = 'crl' then cg.name.nameCrl
                    else cg.name.nameUz
                end,
                case
                    when :lang = 'uz' then cg.description.descriptionUz
                    when :lang = 'ru' then cg.description.descriptionRu
                    when :lang = 'en' then cg.description.descriptionEn
                    when :lang = 'crl' then cg.description.descriptionCrl
                    else cg.description.descriptionUz
                end
            )
            from CourseGradeEntity cg
            where cg.deletedAt is null
        """)
    List<CourseGradeResponseDto> get(String lang);

    @Query("""
        select new uz.asadbek.subcourse.course.grade.dto.OneCourseGradeResponseDto(
            c.id,
            new uz.asadbek.subcourse.util.dto.NameDto(
                c.name.nameUz,
                c.name.nameRu,
                c.name.nameEn,
                c.name.nameCrl
            ),
            new uz.asadbek.subcourse.util.dto.DescriptionDto(
                c.description.descriptionUz,
                c.description.descriptionRu,
                c.description.descriptionEn,
                c.description.descriptionCrl
            )
        )
        from CourseGradeEntity c
        where c.id = :id
        and c.deletedAt is null
        """)
    OneCourseGradeResponseDto get(Long id);
}
