package uz.asadbek.subcourse.test.usertest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTestRepository extends JpaRepository<UserTestEntity, Long> {
    boolean existsByIdUserIdAndIdReferenceId(Long userId, Long courseId);

}
