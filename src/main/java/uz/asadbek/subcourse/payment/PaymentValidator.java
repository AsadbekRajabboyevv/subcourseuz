package uz.asadbek.subcourse.payment;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uz.asadbek.subcourse.util.ExceptionUtil;

@Slf4j
@UtilityClass
public class PaymentValidator {

    public static void validatePurchaseRequest(Long courseId, Long testId) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (courseId != null && testId != null) {
            String message = ExceptionUtil.resolveMessage("error.payment.only_one_product_allowed");
            errors.put("courseId", message);
            errors.put("testId", message);
        }

        if (courseId == null && testId == null) {
            errors.put("product", ExceptionUtil.resolveMessage("required.product"));
        }

        if (!errors.isEmpty()) {
            log.error("[PaymentValidator] Validation errors: {}", errors);
            throw ExceptionUtil.validationException("error.validation", errors);
        }
    }
}
