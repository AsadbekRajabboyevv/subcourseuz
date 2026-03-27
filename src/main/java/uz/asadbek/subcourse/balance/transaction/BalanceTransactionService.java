package uz.asadbek.subcourse.balance.transaction;

import uz.asadbek.subcourse.payment.PaymentEntity;

public interface BalanceTransactionService {

    String createTransaction(PaymentEntity request);                    // system tranzaksiya yaratadi
    BalanceTransactionEntity cancelTransaction(Long paymentId);                       // foydalanuvchi o'zi bekor qiladi
    BalanceTransactionEntity acceptTransaction(Long paymentId);                       // admin qabul qiladi
    BalanceTransactionEntity rejectTransaction(Long paymentId);                       // admin bekor qiladi
    BalanceTransactionEntity failedTransaction(Long paymentId);                       // stsytem tranzaksiya bekor qiladi
}
