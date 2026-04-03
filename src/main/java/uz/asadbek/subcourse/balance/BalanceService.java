package uz.asadbek.subcourse.balance;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.asadbek.subcourse.balance.dto.BalanceResponseDto;
import uz.asadbek.subcourse.balance.filter.BalanceFilter;

public interface BalanceService {

    BalanceResponseDto get(Long userId);
    BalanceResponseDto get();
    Page<BalanceResponseDto> get(Pageable pageable, BalanceFilter filter);
    void debit(Long amount);
    void credit(Long userId, Long amount);
    void addPending(Long userId, Long amount);
    void cancelPending(Long userId, Long amount);
}
