package uz.asadbek.subcourse.payment;

import uz.asadbek.subcourse.payment.dto.PaymentCancelRequestDto;
import uz.asadbek.subcourse.payment.dto.PaymentRequestDto;
import uz.asadbek.subcourse.payment.dto.PaymentResponseDto;

public interface PaymentService {


    PaymentResponseDto purchase(PaymentRequestDto request, Boolean isTopUp);
    PaymentResponseDto cancelPurchase(PaymentCancelRequestDto request);
    void success(String exId);

}
