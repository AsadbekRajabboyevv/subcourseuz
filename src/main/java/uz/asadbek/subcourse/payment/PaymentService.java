package uz.asadbek.subcourse.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.asadbek.subcourse.payment.dto.PaymentAction;
import uz.asadbek.subcourse.payment.dto.PaymentRequestDto;
import uz.asadbek.subcourse.payment.dto.PaymentResponseDto;
import uz.asadbek.subcourse.payment.filter.PaymentFilter;

public interface PaymentService {

    PaymentResponseDto purchase(PaymentRequestDto request, Boolean isTopUp);
    PaymentResponseDto process(String exId, PaymentAction action);

    Page<PaymentResponseDto> get(Pageable pageable, PaymentFilter filter);
    PaymentResponseDto get(String exId);

}
