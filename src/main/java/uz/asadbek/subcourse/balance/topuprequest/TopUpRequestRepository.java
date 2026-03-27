package uz.asadbek.subcourse.balance.topuprequest;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto;
import uz.asadbek.subcourse.balance.topuprequest.filter.TopUpRequestFilter;

@Repository
public interface TopUpRequestRepository extends JpaRepository<TopUpRequestEntity, Long> {

    Optional<TopUpRequestEntity> findByIdAndUserId(Long id, Long userId);

    Optional<TopUpRequestResponseDto> get(Long id, Long userId);

    Page<TopUpRequestResponseDto> get(Long userId, TopUpRequestFilter filter, Pageable pageable);

    Page<TopUpRequestResponseDto> get(Pageable pageable, TopUpRequestFilter filter);

    Optional<TopUpRequestResponseDto> get(Long id);
}
