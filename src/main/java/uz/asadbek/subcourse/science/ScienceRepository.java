package uz.asadbek.subcourse.science;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.science.dto.OneScienceResponseDto;
import uz.asadbek.subcourse.science.dto.ScienceResponseDto;

@Repository
public interface ScienceRepository extends JpaRepository<ScienceEntity, Long> {

    @Query("""
        select new uz.asadbek.subcourse.science.dto.OneScienceResponseDto(
            s.id,
            new uz.asadbek.subcourse.util.dto.NameDto(
                s.name.nameUz,
                s.name.nameRu,
                s.name.nameEn,
                s.name.nameCrl
            ),
            new uz.asadbek.subcourse.util.dto.DescriptionDto(
                s.description.descriptionUz,
                s.description.descriptionRu,
                s.description.descriptionEn,
                s.description.descriptionCrl
            ),
            s.imagePath
        )
        from ScienceEntity s
        where s.id = :id
        and s.deletedAt is null
        """)
    OneScienceResponseDto get(Long id);

    @Query("""
          select new uz.asadbek.subcourse.science.dto.ScienceResponseDto(
             id,
              case
                    when :lang = 'uz' then name.nameUz
                    when :lang = 'ru' then name.nameRu
                    when :lang = 'en' then name.nameEn
                    when :lang = 'crl' then name.nameCrl
                    else name.nameUz
                end,
                case
                    when :lang = 'uz' then description.descriptionUz
                    when :lang = 'ru' then description.descriptionRu
                    when :lang = 'en' then description.descriptionEn
                    when :lang = 'crl' then description.descriptionCrl
                    else description.descriptionUz
                end,
             imagePath
          )
          from ScienceEntity
          where deletedAt is null
    """)
    List<ScienceResponseDto> get(String lang);

}
