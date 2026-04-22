package uz.asadbek.subcourse.balance.topuprequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpBalanceRequestDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestActionRequestDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto;
import uz.asadbek.subcourse.balance.topuprequest.filter.TopUpRequestFilter;
import uz.asadbek.subcourse.payment.dto.PaymentResponseDto;

@RequestMapping("/v1/api/top-up-request")
public interface TopUpRequestApi {

    @GetMapping("/my")
    BaseResponseDto<Page<TopUpRequestResponseDto>> getMy(TopUpRequestFilter filter,
        Pageable pageable);

    @GetMapping("/my/{id}")
    BaseResponseDto<TopUpRequestResponseDto> getMyById(@PathVariable Long id);

    @GetMapping
    BaseResponseDto<Page<TopUpRequestResponseDto>> get(TopUpRequestFilter filter,
        Pageable pageable);

    @PostMapping("/create")
    BaseResponseDto<Long> create(@RequestPart MultipartFile screenshot,
        @RequestPart TopUpBalanceRequestDto request);

    @GetMapping("/{id}")
    BaseResponseDto<TopUpRequestResponseDto> get(@PathVariable Long id);

    @PutMapping("/cancel/{id}")
    BaseResponseDto<Long> cancel(@PathVariable Long id);

    @PutMapping("/admin/accept")
    BaseResponseDto<PaymentResponseDto> accept(@RequestBody TopUpRequestActionRequestDto request);

    @PutMapping("/admin/reject")
    BaseResponseDto<PaymentResponseDto> reject(@RequestBody TopUpRequestActionRequestDto request);
}
