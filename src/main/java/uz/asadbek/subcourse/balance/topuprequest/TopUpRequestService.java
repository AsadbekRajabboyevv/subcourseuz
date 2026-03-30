package uz.asadbek.subcourse.balance.topuprequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpBalanceRequestDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestActionRequestDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto;
import uz.asadbek.subcourse.balance.topuprequest.filter.TopUpRequestFilter;
import uz.asadbek.subcourse.payment.dto.PaymentResponseDto;

public interface TopUpRequestService {

    // USER
    Page<TopUpRequestResponseDto> getMy(Pageable pageable, TopUpRequestFilter filter);

    TopUpRequestResponseDto getMyById(Long id);

    void create(TopUpBalanceRequestDto request, MultipartFile screenshot);

    Long cancel(Long id);

    // ADMIN
    Page<TopUpRequestResponseDto> getAll(Pageable pageable, TopUpRequestFilter filter);

    TopUpRequestResponseDto getById(Long id);

    PaymentResponseDto accept(TopUpRequestActionRequestDto request);

    PaymentResponseDto reject(TopUpRequestActionRequestDto request);
}
