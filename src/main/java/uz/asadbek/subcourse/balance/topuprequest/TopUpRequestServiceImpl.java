package uz.asadbek.subcourse.balance.topuprequest;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.balance.BalanceService;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpBalanceRequestDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpStatus;
import uz.asadbek.subcourse.balance.topuprequest.filter.TopUpRequestFilter;
import uz.asadbek.subcourse.payment.PaymentService;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class TopUpRequestServiceImpl implements TopUpRequestService {

    private final TopUpRequestRepository repository;
    private final BalanceService balanceService;
    private final PaymentService paymentService;

    // ================= USER =================

    @Override
    public Page<TopUpRequestResponseDto> getMy(Pageable pageable, TopUpRequestFilter filter) {
        Long userId = JwtUtil.getCurrentUser().getId();
        repository.findAllByUserId(userId, pageable);
        return null;
    }

    @Override
    public TopUpRequestResponseDto getMyById(Long id) {
        Long userId = JwtUtil.getCurrentUser().getId();

        var entity = repository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> ExceptionUtil.notFoundException("topup_not_found"));

        return null;
    }

    @Override
    @Transactional
    public TopUpRequestResponseDto create(TopUpBalanceRequestDto request) {

        Long userId = JwtUtil.getCurrentUser().getId();
        Long amount = request.getAmount();
        validateAmount(amount);

        balanceService.addPending(userId, amount);

        var entity = TopUpRequestEntity.builder()
            .userId(userId)
            .amount(amount)
            .status(TopUpStatus.PENDING)
            .message(request.getMessage())
            .build();

        repository.save(entity);

        return null;
    }

    @Override
    @Transactional
    public void cancel(Long id) {

        Long userId = JwtUtil.getCurrentUser().getId();

        var entity = repository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> ExceptionUtil.notFoundException("topup_not_found"));
        if (entity.getStatus() != TopUpStatus.PENDING) {
            throw ExceptionUtil.badRequestException("cannot_cancel");
        }

        balanceService.cancelPending(userId, entity.getAmount());

        entity.setStatus(TopUpStatus.CANCELLED);
    }

    // ================= ADMIN =================

    @Override
    public Page<TopUpRequestResponseDto> getAll(Pageable pageable, TopUpRequestFilter filter) {
        repository.findAll(pageable);
        return null;
    }

    @Override
    public TopUpRequestResponseDto getById(Long id) {
        var entity = repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("topup_not_found"));
        return null;
    }

    @Override
    @Transactional
    public TopUpRequestResponseDto accept(Long id) {

        var entity = repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("topup_not_found"));

        if (entity.getStatus() != TopUpStatus.PENDING) {
            throw ExceptionUtil.badRequestException("already_processed");
        }

        balanceService.credit(entity.getUserId(), entity.getAmount());
//        paymentService.success();
        entity.setStatus(TopUpStatus.APPROVED);
        entity.setApprovedAt(LocalDateTime.now());
        return null;
    }

    @Override
    @Transactional
    public TopUpRequestResponseDto reject(Long id) {
        var entity = repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("to_pup_not_found"));

        if (entity.getStatus() != TopUpStatus.PENDING) {
            throw ExceptionUtil.badRequestException("already_processed");
        }

        balanceService.cancelPending(entity.getUserId(), entity.getAmount());

        entity.setStatus(TopUpStatus.REJECTED);
        entity.setRejectedAt(LocalDateTime.now());

        return null;
    }

    private void validateAmount(Long amount) {
        if (amount == null || amount <= 0) {
            throw ExceptionUtil.badRequestException("invalid_amount");
        }
    }
}
