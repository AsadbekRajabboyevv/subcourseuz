package uz.asadbek.subcourse.test.session.option;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.test.session.option.dto.TestSessionOptionResponseDto;

@Repository
public interface TestSessionOptionRepository extends JpaRepository<TestSessionOptionEntity, Long> {

    @Query("""
        SELECT new uz.asadbek.subcourse.test.session.option.dto.TestSessionOptionResponseDto(
          o.originalOptionId,
          o.optionText,
          o.imagePath,
          o.sessionQuestionId
        )
        FROM TestSessionOptionEntity o
        WHERE o.sessionId = :sessionId
        """)
    List<TestSessionOptionResponseDto> findBySessionId(Long sessionId);
}
