package uz.asadbek.subcourse.balance.topuprequest;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.balance.BalanceService;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpBalanceRequestDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestActionRequestDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpRequestResponseDto;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpStatus;
import uz.asadbek.subcourse.balance.topuprequest.filter.TopUpRequestFilter;
import uz.asadbek.subcourse.payment.PaymentService;
import uz.asadbek.subcourse.payment.dto.PaymentAction;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopUpRequestServiceImpl implements TopUpRequestService {

    private final TopUpRequestRepository repository;
    private final BalanceService balanceService;
    private final PaymentService paymentService;

    // ================= USER =================

    @Override
    public Page<TopUpRequestResponseDto> getMy(Pageable pageable, TopUpRequestFilter filter) {
        var userId = JwtUtil.getCurrentUser().getId();
        return repository.get(userId, filter, pageable);
    }

    @Override
    public TopUpRequestResponseDto getMyById(Long id) {
        var userId = JwtUtil.getCurrentUser().getId();
        return repository.get(id, userId)
            .orElseThrow(() -> ExceptionUtil.notFoundException("top_up_not_found"));
    }

    @Override
    @Transactional
    public void create(TopUpBalanceRequestDto request) {

        var userId = JwtUtil.getCurrentUser().getId();
        var amount = request.getAmount();
        validateAmount(amount);

        balanceService.addPending(userId, amount);

        var entity = TopUpRequestEntity.builder()
            .userId(userId)
            .amount(amount)
            .status(TopUpStatus.PENDING)
            .message(request.getMessage())
            .build();

        repository.save(entity);
        log.info("TopUpRequest created: {}", entity);
    }

    @Override
    @Transactional
    public void cancel(Long id) {

        var userId = JwtUtil.getCurrentUser().getId();

        var entity = repository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> ExceptionUtil.notFoundException("top_up_not_found"));
        if (entity.getStatus() != TopUpStatus.PENDING) {
            throw ExceptionUtil.badRequestException("cannot_cancel");
        }

        balanceService.cancelPending(userId, entity.getAmount());

        entity.setStatus(TopUpStatus.CANCELLED);
        log.info("TopUpRequest cancelled: {}", entity);
    }

    // ================= ADMIN =================

    @Override
    public Page<TopUpRequestResponseDto> getAll(Pageable pageable, TopUpRequestFilter filter) {
        return repository.get(pageable, filter);
    }

    @Override
    public TopUpRequestResponseDto getById(Long id) {
        return repository.get(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("top_up_not_found"));
    }

    @Override
    @Transactional
    public void accept(TopUpRequestActionRequestDto request) {
        var entity = repository.findById(request.getId())
            .orElseThrow(() -> ExceptionUtil.notFoundException("topup_not_found"));

        if (entity.getStatus() != TopUpStatus.PENDING) {
            throw ExceptionUtil.badRequestException("already_processed");
        }

        balanceService.credit(entity.getUserId(), entity.getAmount());
        paymentService.process(request.getPaymentExId(), PaymentAction.SUCCESS,
            request.getMessage());
        entity.setStatus(TopUpStatus.APPROVED);
        entity.setApprovedAt(LocalDateTime.now());
        log.info("TopUpRequest approved: {}", entity);
    }

    @Override
    @Transactional
    public void reject(TopUpRequestActionRequestDto request) {
        var entity = findById(request.getId());

        if (entity.getStatus() != TopUpStatus.PENDING) {
            throw ExceptionUtil.badRequestException("already_processed");
        }

        balanceService.cancelPending(entity.getUserId(), entity.getAmount());
        paymentService.process(request.getPaymentExId(), PaymentAction.REJECT,
            request.getMessage());
        entity.setStatus(TopUpStatus.REJECTED);
        entity.setRejectedAt(LocalDateTime.now());
        log.info("TopUpRequest rejected: {}", entity);
    }

    private TopUpRequestEntity findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> ExceptionUtil.notFoundException("top_up_not_found"));
    }

    private void validateAmount(Long amount) {
        if (amount == null || amount <= 0) {
            throw ExceptionUtil.badRequestException("invalid_amount");
        }
    }
}
