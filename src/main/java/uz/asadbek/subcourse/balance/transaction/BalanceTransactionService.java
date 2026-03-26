package uz.asadbek.subcourse.balance.transaction;

import uz.asadbek.subcourse.payment.PaymentEntity;

public interface BalanceTransactionService {

    String createTransaction(PaymentEntity request);                    // system tranzaksiya yaratadi
    void cancelTransaction(String transactionId);                       // foydalanuvchi o'zi bekor qiladi
    void acceptTransaction(Long paymentId);                       // admin qabul qiladi
    void rejectTransaction(String transactionId);                       // admin bekor qiladi
    void failedTransaction(String transactionId);                       // stsytem tranzaksiya bekor qiladi
}
