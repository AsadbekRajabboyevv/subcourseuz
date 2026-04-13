package uz.asadbek.subcourse.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.payment.dto.PaymentRequestDto;
import uz.asadbek.subcourse.payment.dto.PaymentResponseDto;
import uz.asadbek.subcourse.payment.filter.PaymentFilter;

@RequestMapping("/v1/api/payments")
public interface PaymentApi {

    @PostMapping
    BaseResponseDto<PaymentResponseDto> purchase(@RequestBody PaymentRequestDto request);

    @GetMapping
    BaseResponseDto<Page<PaymentResponseDto>> get(PaymentFilter filter, Pageable pageable);

    @GetMapping("/{exId}")
    BaseResponseDto<PaymentResponseDto> get(@PathVariable String exId);

}
