package uz.asadbek.subcourse.test.question;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestionEntity, Long> {

    List<TestQuestionEntity> findByTestId(Long id);
}
