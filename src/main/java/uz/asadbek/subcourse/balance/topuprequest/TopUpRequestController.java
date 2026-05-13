package uz.asadbek.subcourse.balance.topuprequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpBalanceRequestDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestActionRequestDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto;
import uz.asadbek.subcourse.balance.topuprequest.filter.TopUpRequestFilter;
import uz.asadbek.subcourse.payment.dto.PaymentResponseDto;

@RestController
@RequiredArgsConstructor
public class TopUpRequestController implements TopUpRequestApi {

    private final TopUpRequestService service;

    @Override
    public BaseResponseDto<Page<TopUpRequestResponseDto>> getMy(TopUpRequestFilter filter,
        Pageable pageable) {
        return BaseResponseDto.ok(service.getMy(pageable, filter));
    }

    @Override
    public BaseResponseDto<TopUpRequestResponseDto> getMyById(Long id) {
        return BaseResponseDto.ok(service.getMyById(id));
    }

    @Override
    public BaseResponseDto<Page<TopUpRequestResponseDto>> get(TopUpRequestFilter filter,
        Pageable pageable) {
        return BaseResponseDto.ok(service.getAll(pageable, filter));
    }

    @Override
    public BaseResponseDto<Long> create(MultipartFile screenshot, TopUpBalanceRequestDto request) {
        service.create(request, screenshot);
        return BaseResponseDto.ok(0L);
    }

    @Override
    public BaseResponseDto<TopUpRequestResponseDto> get(Long id) {
        return BaseResponseDto.ok(service.getById(id));
    }

    @Override
    public BaseResponseDto<Long> cancel(Long id) {
        return BaseResponseDto.ok(service.cancel(id));
    }

    @Override
    public BaseResponseDto<PaymentResponseDto> accept(TopUpRequestActionRequestDto request) {
        return BaseResponseDto.ok(service.accept(request));
    }

    @Override
    public BaseResponseDto<PaymentResponseDto> reject(TopUpRequestActionRequestDto request) {
        return BaseResponseDto.ok(service.reject(request));
    }
}
