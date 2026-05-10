package uz.asadbek.subcourse.test.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionStatus;

@Repository
public interface TestSessionRepository extends JpaRepository<TestSessionEntity, Long> {

    boolean existsByUserIdAndTestIdAndStatus(Long userId, Long testId, TestSessionStatus status);

    @Query("""
       SELECT new uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto(
           s.id,
           s.testId,
           t.name,
           t.description,
           t.imagePath,
           s.status,
           s.startedAt,
           s.expiresAt,
           s.finishedAt,
           CASE
             WHEN s.finishedAt IS NOT NULL THEN TRUE
             ELSE FALSE
           END
       )
       FROM TestSessionEntity s
       LEFT JOIN TestEntity t ON s.testId = t.id
       WHERE s.id = :sessionId
    """)
    TestSessionResponseDto findByIdAndTestInfo(
        Long sessionId
    );
}
