package uz.asadbek.subcourse.balance.topuprequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpBalanceRequestDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto;
import uz.asadbek.subcourse.balance.topuprequest.filter.TopUpRequestFilter;

public interface TopUpRequestService {

    // USER
    Page<TopUpRequestResponseDto> getMy(Pageable pageable, TopUpRequestFilter filter);

    TopUpRequestResponseDto getMyById(Long id);

    TopUpRequestResponseDto create(TopUpBalanceRequestDto request);

    void cancel(Long id);

    // ADMIN
    Page<TopUpRequestResponseDto> getAll(Pageable pageable, TopUpRequestFilter filter);

    TopUpRequestResponseDto getById(Long id);

    TopUpRequestResponseDto accept(Long id);

    TopUpRequestResponseDto reject(Long id);
}
