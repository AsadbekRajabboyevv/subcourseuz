package uz.asadbek.subcourse.balance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.asadbek.subcourse.balance.dto.BalanceResponseDto;

@Repository
public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {

    @Modifying
    @Query("""
            UPDATE BalanceEntity b
               SET b.pendingBalance = b.pendingBalance + :amount
             WHERE b.userId = :userId
        """)
    int increasePending(Long userId, Long amount);

    @Modifying
    @Query("""
            UPDATE BalanceEntity b
               SET b.balance = b.balance - :amount,
                   b.lastTransactionAt = CURRENT_TIMESTAMP
             WHERE b.userId = :userId
               AND b.balance >= :amount
        """)
    int decrease(Long userId, Long amount);

    @Modifying
    @Query("""
            UPDATE BalanceEntity b
               SET b.balance = b.balance + :amount,
                   b.pendingBalance = b.pendingBalance - :amount
             WHERE b.userId = :userId
               AND b.pendingBalance >= :amount
        """)
    int confirmPending(Long userId, Long amount);

    @Modifying
    @Query("""
            UPDATE BalanceEntity b
               SET b.pendingBalance = b.pendingBalance - :amount
             WHERE b.userId = :userId
               AND b.pendingBalance >= :amount
        """)
    int cancelPending(Long userId, Long amount);

    @Query("""
          SELECT new uz.asadbek.subcourse.balance.dto.BalanceResponseDto(
          b.userId,
          CONCAT(u.firstName, ' ', u.lastName ),
          b.balance,
          b.pendingBalance,
          b.balance + COALESCE(b.pendingBalance, 0),
          b.currency,
          b.lastTransactionAt
          )
          from BalanceEntity b
          LEFT JOIN UserEntity u on b.userId = u.id
          where b.userId = :#{#userId}
        """)
    BalanceResponseDto get(Long userId);

    @Query("""
          SELECT new uz.asadbek.subcourse.balance.dto.BalanceResponseDto(
          b.userId,
          CONCAT(u.firstName, ' ', u.lastName ),
          b.balance,
          b.pendingBalance,
          b.balance + COALESCE(b.pendingBalance, 0),
          b.currency,
          b.lastTransactionAt
          )
          from BalanceEntity b
          LEFT JOIN UserEntity u on b.userId = u.id
        """)
    Page<BalanceResponseDto> get(Pageable pageable);
}
