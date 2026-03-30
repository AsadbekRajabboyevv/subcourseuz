package uz.asadbek.subcourse.payment;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.asadbek.subcourse.balance.BalanceService;
import uz.asadbek.subcourse.balance.dto.CurrencyEnum;
import uz.asadbek.subcourse.balance.transaction.BalanceTransactionEntity;
import uz.asadbek.subcourse.balance.transaction.BalanceTransactionService;
import uz.asadbek.subcourse.course.CourseService;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.payment.dto.PaymentAction;
import uz.asadbek.subcourse.payment.dto.PaymentRequestDto;
import uz.asadbek.subcourse.payment.dto.PaymentResponseDto;
import uz.asadbek.subcourse.payment.dto.PaymentStatus;
import uz.asadbek.subcourse.payment.dto.PaymentType;
import uz.asadbek.subcourse.test.TestService;
import uz.asadbek.subcourse.test.dto.TestResponseDto;
import uz.asadbek.subcourse.util.ExceptionUtil;
import uz.asadbek.subcourse.util.JwtUtil;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final CourseService courseService;
    private final TestService testService;
    private final BalanceTransactionService balanceTransactionService;
    private final PaymentRepository paymentRepository;
    private final BalanceService balanceService;

    public PaymentServiceImpl(CourseService courseService, TestService testService,
        BalanceTransactionService balanceTransactionService, PaymentRepository paymentRepository,
        BalanceService balanceService) {
        this.courseService = courseService;
        this.testService = testService;
        this.balanceTransactionService = balanceTransactionService;
        this.paymentRepository = paymentRepository;
        this.balanceService = balanceService;
    }

    private static PaymentEntity buildPayment(CourseResponseDto course, TestResponseDto test,
        Long amount, CurrencyEnum currency) {
        Long referenceId = null;
        PaymentType type = null;
        if (course != null) {
            referenceId = course.getId();
            type = PaymentType.COURSE;
        } else if (test != null) {
            referenceId = test.getId();
            type = PaymentType.TEST;
        } else {
            type = PaymentType.TOP_UP;
        }

        return PaymentEntity.builder()
            .type(type)
            .amount(amount)
            .userId(JwtUtil.getCurrentUser().getId())
            .referenceId(referenceId)
            .currency(currency)
            .status(PaymentStatus.PROCESSING)
            .exId(generateExId(type))
            .build();
    }

    private static String generateExId(PaymentType type) {
        return STR."payment-\{type.name()}\{System.currentTimeMillis()}";
    }

    @Override
    @Transactional
    public PaymentResponseDto purchase(PaymentRequestDto request, Boolean isTopUp) {

        var courseId = request.getCourseId();
        var testId = request.getTestId();
        var couponCode = request.getCouponCode();
        var currentUserId = JwtUtil.getCurrentUser().getId();
        var amount = request.getAmount() != null ? request.getAmount() : 0L;
        TestResponseDto test = null;
        CourseResponseDto course = null;
        var transactionId = "";
        Long savedPaymentId = null;
        var balance = balanceService.get(currentUserId);
        try {
            if (courseId != null && testId != null) {
                throw ExceptionUtil.badRequestException("only_one_product_allowed");
            }

            if (courseId == null && testId == null) {
                throw ExceptionUtil.badRequestException("product_required");
            }

            if (courseId != null) {
                course = courseService.get(courseId);
                amount = course.getPrice();
            } else {
                test = testService.get(testId);
                amount = test.getPrice();
            }

            if ("free".equalsIgnoreCase(couponCode)) {
                amount = 0L;
            }

            if (!isTopUp) {
                balanceService.debit(currentUserId, amount);
            }

            var payment = buildPayment(course, test, amount, balance.getCurrency());
            var savedPayment = paymentRepository.save(payment);
            savedPaymentId = savedPayment.getId();
            transactionId = balanceTransactionService.createTransaction(savedPayment);

            return PaymentResponseDto.builder()
                .exId(savedPayment.getExId())
                .amount(amount)
                .transactionId(transactionId)
                .status(savedPayment.getStatus().name())
                .build();

        } catch (Exception e) {
            balanceTransactionService.cancelTransaction(savedPaymentId);
            throw ExceptionUtil.badRequestException("payment_failed");
        }
    }

    @Transactional
    public PaymentResponseDto process(String exId, PaymentAction action) {

        var payment = paymentRepository.findByExId(exId)
            .orElseThrow(() -> ExceptionUtil.notFoundException("payment_not_found"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            if (action == PaymentAction.CANCEL) {
                throw ExceptionUtil.badRequestException("payment_already_success");
            }
            return buildResponse(payment, null);
        }

        if (payment.getStatus() != PaymentStatus.PROCESSING &&
            payment.getStatus() != PaymentStatus.CREATED) {

            throw ExceptionUtil.badRequestException("invalid_payment_status");
        }

        payment.setCompletedAt(LocalDateTime.now());

        BalanceTransactionEntity transaction = null;

        switch (action) {
            case SUCCESS -> {
                payment.setStatus(PaymentStatus.SUCCESS);
                transaction = balanceTransactionService.acceptTransaction(payment.getId());
            }

            case REJECT -> {
                payment.setStatus(PaymentStatus.FAILED);
                transaction = balanceTransactionService.rejectTransaction(payment.getId());
            }
        }

        paymentRepository.save(payment);

        return buildResponse(payment, transaction);
    }

    private PaymentResponseDto buildResponse(PaymentEntity payment,
        BalanceTransactionEntity transaction) {
        return PaymentResponseDto.builder()
            .exId(payment.getExId())
            .status(payment.getStatus().name())
            .transactionId(transaction != null ? transaction.getExternalTx() : null)
            .amount(payment.getAmount())
            .currency(payment.getCurrency())
            .build();
    }
}
