package uz.asadbek.subcourse.balance.transaction;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceTransactionRepository extends JpaRepository<BalanceTransactionEntity, Long> {

    Optional<BalanceTransactionEntity> findByPaymentId(Long id);

}
