package uz.asadbek.subcourse.payment;

import org.springframework.stereotype.Service;
import uz.asadbek.subcourse.balance.BalanceService;
import uz.asadbek.subcourse.balance.transaction.BalanceTransactionService;
import uz.asadbek.subcourse.course.CourseService;
import uz.asadbek.subcourse.course.dto.CourseResponseDto;
import uz.asadbek.subcourse.payment.dto.PaymentCancelRequestDto;
import uz.asadbek.subcourse.payment.dto.PaymentRequestDto;
import uz.asadbek.subcourse.payment.dto.PaymentResponseDto;
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

    @Override
    public PaymentResponseDto purchase(PaymentRequestDto request) {

        var courseId = request.getCourseId();
        var testId = request.getTestId();
        var couponCode = request.getCouponCode();
        var currentUserId = JwtUtil.getCurrentUser().getId();
        Long amount;
        TestResponseDto test = null;
        CourseResponseDto course = null;

        try {
            // 1. validate (faqat bittasi bo‘lishi kerak)
            if (courseId != null && testId != null) {
                throw ExceptionUtil.badRequestException("only_one_product_allowed");
            }

            if (courseId == null && testId == null) {
                throw ExceptionUtil.badRequestException("product_required");
            }

            // 2. product olish
            if (courseId != null) {
                course = courseService.get(courseId);
                amount = course.getPrice();
            } else {
                test = testService.get(testId);
                amount = test.getPrice();
            }

            // 3. coupon
            if ("free".equalsIgnoreCase(couponCode)) {
                amount = 0L;
            }

            // 🔥 4. balance check + debit (ENG MUHIM)
            balanceService.debit(currentUserId, amount);

            // 5. payment yaratish
            var payment = buildPayment(course, test, amount);
            var savedPayment = paymentRepository.save(payment);

            // 6. transaction yozish
            var transactionId = balanceTransactionService.createTransaction(savedPayment);

            // 7. response
            return PaymentResponseDto.builder()
                .exId(savedPayment.getExId())
                .amount(amount)
                .transactionId(transactionId)
                .status(savedPayment.getStatus().name())
                .build();

        } catch (Exception e) {
            balanceTransactionService.cancelTransaction();
            throw ExceptionUtil.badRequestException("payment_failed");
        }
    }
    private static PaymentEntity buildPayment(CourseResponseDto course, TestResponseDto test, Long amount) {
        Long referenceId = null;
        PaymentType type = null;
        if (course != null) {
            referenceId = course.getId();
            type = PaymentType.COURSE;
        } else if (test != null) {
            referenceId = test.getId();
            type = PaymentType.TEST;
        }
        return PaymentEntity.builder()
            .type(type)
            .amount(amount)
            .userId(JwtUtil.getCurrentUser().getId())
            .referenceId(referenceId)
            .exId(generateExId(type))
            .build();
    }

    private static String generateExId(PaymentType type) {
        return STR."payment-\{type.name()}\{System.currentTimeMillis()}";
    }
    @Override
    public PaymentResponseDto cancelPurchase(PaymentCancelRequestDto request) {
        return null;
    }
}
