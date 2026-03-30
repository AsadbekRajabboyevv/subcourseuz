package uz.asadbek.subcourse.balance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.asadbek.base.dto.BaseResponseDto;
import uz.asadbek.subcourse.balance.dto.BalanceResponseDto;
import uz.asadbek.subcourse.balance.filter.BalanceFilter;

@RequestMapping("/v1/api/balance")
public interface BalanceApi {

    @GetMapping("/my-balance")
    BaseResponseDto<BalanceResponseDto> getMyBalance();

    @GetMapping("/{userId}")
    BaseResponseDto<BalanceResponseDto> getBalance(@PathVariable Long userId);

    @GetMapping
    BaseResponseDto<Page<BalanceResponseDto>> get(Pageable pageable, BalanceFilter filter);
}
