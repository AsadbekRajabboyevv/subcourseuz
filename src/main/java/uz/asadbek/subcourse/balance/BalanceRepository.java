package uz.asadbek.subcourse.balance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {

    BalanceEntity findByUserId(Long userId);
    @Modifying
    @Query("""
    UPDATE BalanceEntity b
       SET b.balance = b.balance - :amount
     WHERE b.userId = :userId
       AND b.balance >= :amount
""")
    int decreaseBalance(Long userId, Long amount);
}
