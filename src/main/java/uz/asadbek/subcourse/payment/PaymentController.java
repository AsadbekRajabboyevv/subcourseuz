package uz.asadbek.subcourse.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.payment.dto.PaymentRequestDto;
import uz.asadbek.subcourse.payment.dto.PaymentResponseDto;
import uz.asadbek.subcourse.payment.filter.PaymentFilter;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi{

    private final PaymentService service;

    @Override
    public BaseResponseDto<PaymentResponseDto> purchase(PaymentRequestDto request) {
        return BaseResponseDto.ok(service.purchase(request, false));
    }

    @Override
    public BaseResponseDto<Page<PaymentResponseDto>> get(PaymentFilter filter, Pageable pageable) {
        return BaseResponseDto.ok(service.get(pageable, filter));
    }

    @Override
    public BaseResponseDto<PaymentResponseDto> get(String exId) {
        return BaseResponseDto.ok(service.get(exId));
    }

}
