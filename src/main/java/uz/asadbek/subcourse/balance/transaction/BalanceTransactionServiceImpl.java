package uz.asadbek.subcourse.balance.transaction;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.balance.BalanceService;
import uz.asadbek.subcourse.balance.dto.TransactionStatus;
import uz.asadbek.subcourse.balance.dto.TransactionType;
import uz.asadbek.subcourse.balance.transaction.dto.BalanceTransactionRequestDto;
import uz.asadbek.subcourse.payment.PaymentEntity;
import uz.asadbek.subcourse.payment.dto.PaymentType;
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
    @Transactional
    public String createTransaction(PaymentEntity payment) {

        Long userId = payment.getUserId();
        Long amount = payment.getAmount();

        Optional<BalanceTransaction> existing =
            balanceTransactionRepository.findByPaymentId(payment.getId());

        if (existing.isPresent()) {
            return existing.get().getExternalTx();
        }

        Long balanceAfter = balanceService.get(userId).getBalance();
        Long balanceBefore;

        TransactionType transactionType;

        // 🔥 TYPE bo‘yicha ajratamiz
        switch (payment.getType()) {

            case TOP_UP -> {
                transactionType = TransactionType.TOP_UP;

                // CREDIT: pul qo‘shildi
                balanceBefore = balanceAfter - amount;
            }

            case COURSE -> {
                transactionType = TransactionType.COURSE_PURCHASE;
                balanceBefore = balanceAfter + amount;
            }

            case TEST -> {
                transactionType = TransactionType.TEST_PURCHASE;

                balanceBefore = balanceAfter + amount;
            }

            default -> throw ExceptionUtil.badRequestException("unknown_payment_type");
        }

        BalanceTransaction tx = new BalanceTransaction();
        tx.setExternalTx(generateExternalTx());
        tx.setUserId(userId);
        tx.setStatus(TransactionStatus.SUCCESS);
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
    public void cancelTransaction(Long transactionId) {

    }

    @Override
    public void acceptTransaction(Long transactionId) {

    }

    @Override
    public void rejectTransaction(Long transactionId) {

    }

    @Override
    public void failedTransaction(Long transactionId) {

    }
}
