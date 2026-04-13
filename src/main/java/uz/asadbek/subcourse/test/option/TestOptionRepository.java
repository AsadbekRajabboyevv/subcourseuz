package uz.asadbek.subcourse.test.option;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.test.option.dto.TestOptionResponseDto;

@Repository
public interface TestOptionRepository extends JpaRepository<TestOptionEntity, Long> {

    List<TestOptionEntity> findByQuestionId(Long id);

    @Query("""
            SELECT new uz.asadbek.subcourse.test.option.dto.TestOptionResponseDto(
                o.id,
                o.text,
                o.imagePath,
                o.questionId
            )
            FROM TestOptionEntity o
            WHERE o.questionId IN :questionIds
        """)
    List<TestOptionResponseDto> getByQuestionIds(List<Long> questionIds);
}
