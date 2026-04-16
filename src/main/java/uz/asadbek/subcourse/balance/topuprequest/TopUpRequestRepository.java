package uz.asadbek.subcourse.balance.topuprequest;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto;
import uz.asadbek.subcourse.balance.topuprequest.filter.TopUpRequestFilter;

@Repository
public interface TopUpRequestRepository extends JpaRepository<TopUpRequestEntity, Long> {

    Optional<TopUpRequestEntity> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT new uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto(
               t.id,
               t.amount,
               t.message,
               t.status,
               bt.externalTx,
               t.userId,
               CONCAT(u.firstName,' ', u.lastName),
               t.fileKey,
               t.comment,
               t.approvedBy,
               CONCAT(a.firstName,' ', a.lastName),
               t.approvedAt,
               t.rejectedAt,
               p.exId,
               t.createdAt,
               t.updatedAt,
               t.deletedAt,
               t.createdBy,
               t.updatedBy,
               t.deletedBy
            )
            FROM TopUpRequestEntity t
            LEFT JOIN UserEntity u ON u.id = t.userId
            LEFT JOIN UserEntity a ON a.id = t.approvedBy
            LEFT JOIN PaymentEntity p ON t.paymentId = p.id
            LEFT JOIN BalanceTransactionEntity bt ON bt.paymentId = t.paymentId
            WHERE t.deletedAt IS NULL
            AND t.id = :id
            AND t.userId = :userId
        """)
    Optional<TopUpRequestResponseDto> get(Long id, Long userId);

    @Query("""
            SELECT new uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto(
               t.id,
               t.amount,
               t.message,
               t.status,
               bt.externalTx,
               t.userId,
               CONCAT(u.firstName,' ', u.lastName),
               t.fileKey,
               t.comment,
               t.approvedBy,
               CONCAT(a.firstName,' ', a.lastName),
               t.approvedAt,
               t.rejectedAt,
               p.exId,
               t.createdAt,
               t.updatedAt,
               t.deletedAt,
               t.createdBy,
               t.updatedBy,
               t.deletedBy
            )
            FROM TopUpRequestEntity t
            LEFT JOIN UserEntity u ON u.id = t.userId
            LEFT JOIN UserEntity a ON a.id = t.approvedBy
            LEFT JOIN PaymentEntity p ON t.paymentId = p.id
            LEFT JOIN BalanceTransactionEntity bt ON bt.paymentId = t.paymentId
            WHERE t.deletedAt IS NULL
            AND t.id = :id
            AND t.userId = :userId
            AND (:#{#filter.status} IS NULL OR t.status = :#{#filter.status})
            AND (:#{#filter.transactionId} IS NULL OR bt.externalTx = :#{#filter.transactionId})
            AND (:#{#filter.message} IS NULL OR LOWER(t.message) LIKE LOWER(CONCAT('%', :#{#filter.message}, '%')))
            AND (:#{#filter.amountFrom} IS NULL OR t.amount >= :#{#filter.amountFrom})
            AND (:#{#filter.amountTo} IS NULL OR t.amount <= :#{#filter.amountTo})
            AND (:#{#filter.createdAtFrom} IS NULL OR t.createdAt >= :#{#filter.createdAtFrom})
            AND (:#{#filter.createdAtTo} IS NULL OR t.createdAt <= :#{#filter.createdAtTo})
            AND (:#{#filter.updatedAtFrom} IS NULL OR t.updatedAt >= :#{#filter.updatedAtFrom})
            AND (:#{#filter.updatedAtTo} IS NULL OR t.updatedAt <= :#{#filter.updatedAtTo})
        """)
    Page<TopUpRequestResponseDto> get(Long userId, TopUpRequestFilter filter, Pageable pageable);

    @Query("""
            SELECT new uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto(
               t.id,
               t.amount,
               t.message,
               t.status,
               bt.externalTx,
               t.userId,
               CONCAT(u.firstName,' ', u.lastName),
               t.fileKey,
               t.comment,
               t.approvedBy,
               CONCAT(a.firstName,' ', a.lastName),
               t.approvedAt,
               t.rejectedAt,
               p.exId,
               t.createdAt,
               t.updatedAt,
               t.deletedAt,
               t.createdBy,
               t.updatedBy,
               t.deletedBy
            )
            FROM TopUpRequestEntity t
            LEFT JOIN UserEntity u ON u.id = t.userId
            LEFT JOIN UserEntity a ON a.id = t.approvedBy
            LEFT JOIN PaymentEntity p ON t.paymentId = p.id
            LEFT JOIN BalanceTransactionEntity bt ON bt.paymentId = t.paymentId
            WHERE t.deletedAt IS NULL
            AND (:#{#filter.status} IS NULL OR t.status = :#{#filter.status})
            AND (:#{#filter.transactionId} IS NULL OR bt.externalTx = :#{#filter.transactionId})
            AND (:#{#filter.message} IS NULL OR LOWER(t.message) LIKE LOWER(CONCAT('%', :#{#filter.message}, '%')))
            AND (:#{#filter.amountFrom} IS NULL OR t.amount >= :#{#filter.amountFrom})
            AND (:#{#filter.amountTo} IS NULL OR t.amount <= :#{#filter.amountTo})
            AND (:#{#filter.createdAtFrom} IS NULL OR t.createdAt >= :#{#filter.createdAtFrom})
            AND (:#{#filter.createdAtTo} IS NULL OR t.createdAt <= :#{#filter.createdAtTo})
            AND (:#{#filter.updatedAtFrom} IS NULL OR t.updatedAt >= :#{#filter.updatedAtFrom})
            AND (:#{#filter.updatedAtTo} IS NULL OR t.updatedAt <= :#{#filter.updatedAtTo})
        """)
    Page<TopUpRequestResponseDto> get(Pageable pageable, TopUpRequestFilter filter);

    @Query("""
            SELECT new uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto(
               t.id,
               t.amount,
               t.message,
               t.status,
               bt.externalTx,
               t.userId,
               CONCAT(u.firstName,' ', u.lastName),
               t.fileKey,
               t.comment,
               t.approvedBy,
               CONCAT(a.firstName,' ', a.lastName),
               t.approvedAt,
               t.rejectedAt,
               p.exId,
               t.createdAt,
               t.updatedAt,
               t.deletedAt,
               t.createdBy,
               t.updatedBy,
               t.deletedBy
            )
            FROM TopUpRequestEntity t
            LEFT JOIN UserEntity u ON u.id = t.userId
            LEFT JOIN UserEntity a ON a.id = t.approvedBy
            LEFT JOIN PaymentEntity p ON t.paymentId = p.id
            LEFT JOIN BalanceTransactionEntity bt ON bt.paymentId = t.paymentId
            WHERE t.deletedAt IS NULL
            AND t.id = :id
        """)
    Optional<TopUpRequestResponseDto> get(Long id);
}
