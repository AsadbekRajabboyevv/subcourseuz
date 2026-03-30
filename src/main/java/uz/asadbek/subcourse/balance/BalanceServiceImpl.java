package uz.asadbek.subcourse.balance;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.balance.dto.BalanceResponseDto;
import uz.asadbek.subcourse.balance.filter.BalanceFilter;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository repository;

    @Override
    public BalanceResponseDto get(Long userId) {
        return repository.get(userId);
    }

    @Override
    public BalanceResponseDto get() {
        Long currentUser = JwtUtil.getCurrentUser().getId();
        return repository.get(currentUser);
    }

    @Override
    public Page<BalanceResponseDto> get(Pageable pageable, BalanceFilter filter) {
        return repository.get(pageable, filter);
    }

    @Override
    @Transactional
    public void debit(Long userId, Long amount) {

        validate(userId, amount);

        int updated = repository.decrease(userId, amount);

        if (updated == 0) {
            throw ExceptionUtil.badRequestException("hold_not_found_or_insufficient");
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
    public void cancelPending(Long userId, Long amount) {

        validate(userId, amount);

        repository.cancelPending(userId, amount);
    }

    private void validate(Long userId, Long amount) {
        if (userId == null || amount == null || amount <= 0) {
            throw ExceptionUtil.insufficientBalanceException("invalid_amount");
        }
    }

}
