package uz.asadbek.subcourse.test.session;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.test.session.dto.TestSessionResponseDto;
import uz.asadbek.subcourse.test.session.dto.TestSessionStatus;
import uz.asadbek.subcourse.test.session.dto.UserTestSessionResponseDto;

@Repository
public interface TestSessionRepository extends JpaRepository<TestSessionEntity, Long> {

    @Query("SELECT s.id FROM TestSessionEntity s WHERE s.userId = :userId AND s.testId = :testId AND s.status = :status")
    Optional<Long> findIdByUserIdAndTestIdAndStatus(Long userId, Long testId,
        TestSessionStatus status);

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
               coalesce(u.firstName, ' ', u.lastName),
               t.lang,
               sc.name.nameUz,
               cg.name.nameUz,
               CASE
                 WHEN s.finishedAt IS NOT NULL THEN TRUE
                 ELSE FALSE
               END,
               t.maxScore,
               t.enabledViewCorrectAnswers
           )
           FROM TestSessionEntity s
           LEFT JOIN TestEntity t ON s.testId = t.id
           LEFT JOIN UserEntity u ON s.createdBy = u.id
           LEFT JOIN ScienceEntity sc ON t.scienceId = sc.id
           LEFT JOIN CourseGradeEntity cg ON t.gradeId = cg.id
           WHERE s.id = :sessionId
        """)
    TestSessionResponseDto findByIdAndTestInfo(
        Long sessionId
    );

    @Query("""
            SELECT new uz.asadbek.subcourse.test.session.dto.UserTestSessionResponseDto(
                s.id,
                s.testId,
                t.name,
                s.status,
                s.correctAnswers,
                s.wrongAnswers,
                s.score,
                s.maxScore,
                s.startedAt,
                s.finishedAt
            )
            FROM TestSessionEntity s
            LEFT JOIN TestEntity t ON s.testId = t.id
            WHERE s.userId = :userId
            ORDER BY s.id desc
        """)
    Page<UserTestSessionResponseDto> findAllByUserId(Long userId, Pageable pageable);
}
