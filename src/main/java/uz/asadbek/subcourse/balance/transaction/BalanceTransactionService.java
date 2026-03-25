package uz.asadbek.subcourse.balance.transaction;

import uz.asadbek.subcourse.payment.PaymentEntity;

public interface BalanceTransactionService {

    String createTransaction(PaymentEntity request);     // system tranzaksiya yaratadi
    void cancelTransaction(Long transactionId);                       // foydalanuvchi o'zi bekor qiladi
    void acceptTransaction(Long transactionId);                       // admin qabul qiladi
    void rejectTransaction(Long transactionId);                       // admin bekor qiladi
    void failedTransaction(Long transactionId);                       // stsytem tranzaksiya bekor qiladi
}
