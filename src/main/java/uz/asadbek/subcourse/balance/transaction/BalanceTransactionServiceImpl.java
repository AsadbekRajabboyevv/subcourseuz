package uz.asadbek.subcourse.balance.transaction;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.balance.BalanceService;
import uz.asadbek.subcourse.balance.dto.TransactionStatus;
import uz.asadbek.subcourse.balance.dto.TransactionType;
import uz.asadbek.subcourse.payment.PaymentEntity;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Service
public class BalanceTransactionServiceImpl implements BalanceTransactionService {

    private final BalanceTransactionRepository balanceTransactionRepository;
    private final BalanceService balanceService;

    public BalanceTransactionServiceImpl(BalanceTransactionRepository balanceTransactionRepository,
        BalanceService balanceService) {
        this.balanceTransactionRepository = balanceTransactionRepository;
        this.balanceService = balanceService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createTransaction(PaymentEntity payment) {

        Long userId = payment.getUserId();
        Long amount = payment.getAmount() !=null ? payment.getAmount() : 0L;

        Optional<BalanceTransactionEntity> existing =
            balanceTransactionRepository.findByPaymentId(payment.getId());

        if (existing.isPresent()) {
            return existing.get().getExternalTx();
        }
        BalanceTransactionEntity tx = new BalanceTransactionEntity();
        Long balanceAfter = balanceService.get(userId).getBalance();
        Long balanceBefore;

        TransactionType transactionType;

        // 🔥 TYPE bo‘yicha ajratamiz
        switch (payment.getType()) {

            case TOP_UP -> {
                transactionType = TransactionType.TOP_UP;
                balanceBefore = balanceAfter - amount;
                tx.setStatus(TransactionStatus.PENDING);
            }

            case COURSE -> {
                transactionType = TransactionType.COURSE_PURCHASE;
                balanceBefore = balanceAfter + amount;
                tx.setStatus(TransactionStatus.SUCCESS);
            }

            case TEST -> {
                transactionType = TransactionType.TEST_PURCHASE;
                balanceBefore = balanceAfter + amount;
                tx.setStatus(TransactionStatus.SUCCESS);
            }

            default -> throw ExceptionUtil.badRequestException("unknown_payment_type");
        }

        tx.setExternalTx(generateExternalTx());
        tx.setUserId(userId);
        tx.setPaymentId(payment.getId());
        tx.setTransactionType(transactionType);
        tx.setAmount(amount);

        tx.setBalanceBefore(balanceBefore);
        tx.setBalanceAfter(balanceAfter);

        tx.setDescription("Payment transaction");
        tx.setReferenceId(payment.getReferenceId());

        balanceTransactionRepository.save(tx);

        return tx.getExternalTx();
    }

    private String generateExternalTx() {
        return STR."TXN-\{UUID.randomUUID()}";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BalanceTransactionEntity cancelTransaction(Long paymentId) {
        return save(paymentId, TransactionStatus.CANCELLED);
    }

    private BalanceTransactionEntity save(Long paymentId, TransactionStatus status) {
        var transaction = balanceTransactionRepository.findByPaymentId(
                paymentId)
            .orElseThrow(() -> ExceptionUtil.notFoundException("transaction_not_found"));
        transaction.setStatus(status);
        transaction.setCompletedAt(LocalDateTime.now());
       return balanceTransactionRepository.save(transaction);
    }

    @Override
    public BalanceTransactionEntity acceptTransaction(Long paymentId) {
        return save(paymentId, TransactionStatus.SUCCESS);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BalanceTransactionEntity rejectTransaction(Long paymentId) {
        return save(paymentId, TransactionStatus.REJECTED);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BalanceTransactionEntity failedTransaction(Long paymentId) {
       return save(paymentId, TransactionStatus.FAILED);
    }
}
