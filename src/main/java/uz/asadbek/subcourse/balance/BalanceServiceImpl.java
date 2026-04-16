package uz.asadbek.subcourse.balance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.balance.dto.BalanceResponseDto;
import uz.asadbek.subcourse.balance.dto.CurrencyEnum;
import uz.asadbek.subcourse.balance.filter.BalanceFilter;
import uz.asadbek.subcourse.user.UserEntity;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository repository;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BalanceResponseDto get(Long userId) {
        return repository.get(userId);
    }

    @Override
    public BalanceResponseDto get() {
        Long currentUser = JwtUtil.getCurrentUser().getId();
        return repository.get(currentUser);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<BalanceResponseDto> get(Pageable pageable, BalanceFilter filter) {
        return repository.get(pageable, filter);
    }

    @Override
    @Transactional
    public void debit(Long amount) {
        var userId = JwtUtil.getCurrentUser().getId();
        validate(userId, amount);

        int updated = repository.decrease(userId, amount);

        if (updated == 0) {
            throw ExceptionUtil.insufficientBalanceException("hold_not_found_or_insufficient");
        }
    }


    @Override
    @Transactional
    public void credit(Long userId, Long amount) {

        validate(userId, amount);

        int updated = repository.confirmPending(userId, amount);

        if (updated == 0) {
            throw ExceptionUtil.badRequestException("pending_not_found");
        }
    }

    @Override
    @Transactional
    public void addPending(Long userId, Long amount) {

        validate(userId, amount);

        repository.increasePending(userId, amount);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void cancelPending(Long userId, Long amount) {

        validate(userId, amount);

        repository.cancelPending(userId, amount);
    }

    @Override
    @Transactional
    public void createBalance(UserEntity user) {
        var balance = new BalanceEntity();
        balance.setUserId(user.getId());
        balance.setCurrency(CurrencyEnum.UZS);
        repository.save(balance);
    }

    private void validate(Long userId, Long amount) {
        if (userId == null || amount == null || amount <= 0) {
            log.error("Invalid user or amount: userId={}, amount={}", userId, amount);
            throw ExceptionUtil.insufficientBalanceException("invalid_amount");
        }
    }

}
