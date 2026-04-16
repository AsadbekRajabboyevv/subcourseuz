package uz.asadbek.subcourse.payment;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.payment.dto.PaymentResponseDto;
import uz.asadbek.subcourse.payment.filter.PaymentFilter;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByExId(String exId);

    @Query("""
            SELECT NEW uz.asadbek.subcourse.payment.dto.PaymentResponseDto(
              p.exId,
              p.status,
              t.externalTx,
              p.amount,
              p.currency,
              p.type
            )
            FROM PaymentEntity p
            LEFT JOIN BalanceTransactionEntity t on p.id = t.paymentId
            WHERE p.deletedAt is null
            AND (:#{#filter.id} IS NULL OR p.id = :#{#filter.id})
            AND (:#{#filter.userId} IS NULL OR p.userId = :#{#filter.userId})
            AND (:#{#filter.status} IS NULL OR p.status = :#{#filter.status})
            AND (:#{#filter.type} IS NULL OR p.type = :#{#filter.type})
            AND (:#{#filter.createdAtFrom} IS NULL OR p.createdAt >= :#{#filter.createdAtFrom})
            AND (:#{#filter.createdAtTo} IS NULL OR p.createdAt <= :#{#filter.createdAtTo})
            AND (:#{#filter.updatedAtFrom} IS NULL OR p.updatedAt >= :#{#filter.updatedAtFrom})
            AND (:#{#filter.updatedAtTo} IS NULL OR p.updatedAt <= :#{#filter.updatedAtTo})
            AND (:#{#filter.completedAtFrom} IS NULL OR p.completedAt >= :#{#filter.completedAtFrom})
            AND (:#{#filter.completedAtTo} IS NULL OR p.completedAt <= :#{#filter.completedAtTo})
            AND (:#{#filter.createdBy} IS NULL OR p.createdBy = :#{#filter.createdBy})
            AND (:#{#filter.updatedBy} IS NULL OR p.updatedBy = :#{#filter.updatedBy})
        """)
    Page<PaymentResponseDto> get(PaymentFilter filter, Pageable pageable);

    @Query("""
            SELECT NEW uz.asadbek.subcourse.payment.dto.PaymentResponseDto(
              p.exId,
              p.status,
              t.externalTx,
              p.amount,
              p.currency,
              p.type
            )
            FROM PaymentEntity p
            LEFT JOIN BalanceTransactionEntity t on p.id = t.paymentId
            WHERE p.deletedAt is null
            AND p.exId = :id
            AND (:userId IS NULL OR p.userId = :userId)
        """)
    PaymentResponseDto get(String id, Long userId);
}
