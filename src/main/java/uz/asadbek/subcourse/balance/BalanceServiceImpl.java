package uz.asadbek.subcourse.balance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.asadbek.subcourse.balance.dto.AcceptBalanceResponseDto;
import uz.asadbek.subcourse.balance.dto.BalanceResponseDto;
import uz.asadbek.subcourse.balance.dto.CancelBalanceResponseDto;
import uz.asadbek.subcourse.balance.dto.TopUpBalanceRequestDto;
import uz.asadbek.subcourse.balance.dto.TopUpBalanceResponseDto;
import uz.asadbek.subcourse.exception.InsufficientBalanceException;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceServiceImpl(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    public BalanceResponseDto get(Long userId) {
        return null;
    }

    @Override
    public BalanceResponseDto get() {
        return null;
    }

    @Override
    public Page<UserBalancesResponseDto> get(Pageable pageable) {
        return null;
    }

    @Override
    public TopUpBalanceResponseDto topUpBalance(TopUpBalanceRequestDto request,
        MultipartFile screenshot) {
        return null;
    }

    @Override
    public AcceptBalanceResponseDto acceptBalance(Long transactionId) {
        return null;
    }

    @Override
    public CancelBalanceResponseDto cancelBalance(Long transactionId) {
        return null;
    }

    @Override
    @Transactional
    public void debit(Long userId, Long amount) {
        if (userId == null || amount == null || amount <= 0) {
            throw ExceptionUtil.badRequestException("invalid_amount");
        }

        int updated = balanceRepository.decreaseBalance(userId, amount);

        if (updated == 0) {
            throw  ExceptionUtil.insufficientBalanceException("insufficient_balance");
        }
    }
}
