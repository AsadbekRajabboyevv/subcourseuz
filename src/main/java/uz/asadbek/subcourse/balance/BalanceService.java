package uz.asadbek.subcourse.balance;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.balance.dto.AcceptBalanceResponseDto;
import uz.asadbek.subcourse.balance.dto.BalanceResponseDto;
import uz.asadbek.subcourse.balance.dto.CancelBalanceResponseDto;
import uz.asadbek.subcourse.balance.dto.TopUpBalanceRequestDto;
import uz.asadbek.subcourse.balance.dto.TopUpBalanceResponseDto;

public interface BalanceService {

    BalanceResponseDto get(Long userId);
    BalanceResponseDto get();
    Page<UserBalancesResponseDto> get(Pageable pageable);
    TopUpBalanceResponseDto topUpBalance(TopUpBalanceRequestDto request, MultipartFile screenshot);

    AcceptBalanceResponseDto acceptBalance(Long transactionId);  // Admin balansni qabul qiladi
    CancelBalanceResponseDto cancelBalance(Long transactionId);  // Admin balansni bekor qiladi

    void debit(Long userId, Long amount);
}
