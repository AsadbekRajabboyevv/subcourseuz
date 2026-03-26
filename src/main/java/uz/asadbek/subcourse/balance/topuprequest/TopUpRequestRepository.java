package uz.asadbek.subcourse.balance.topuprequest;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopUpRequestRepository extends JpaRepository<TopUpRequestEntity, Long> {

    Optional<TopUpRequestEntity> findByIdAndUserId(Long id, Long userId);

    Page<TopUpRequestEntity> findAllByUserId(Long userId, Pageable pageable);
}
