package uz.asadbek.subcourse.balance;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.balance.dto.BalanceResponseDto;
import uz.asadbek.subcourse.balance.filter.BalanceFilter;

@RestController
@RequiredArgsConstructor
public class BalanceController implements BalanceApi {

    private final BalanceService service;

    @Override
    public BaseResponseDto<BalanceResponseDto> getMyBalance() {
        return BaseResponseDto.ok(service.get());
    }

    @Override
    public BaseResponseDto<BalanceResponseDto> getBalance(Long userId) {
        return BaseResponseDto.ok(service.get(userId));
    }

    @Override
    public BaseResponseDto<Page<BalanceResponseDto>> get(Pageable pageable, BalanceFilter filter) {
        return BaseResponseDto.ok(service.get(pageable, filter));
    }
}
