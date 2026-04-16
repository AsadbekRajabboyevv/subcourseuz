package uz.asadbek.subcourse.payment.filter;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import uz.asadbek.base.filter.BaseFilter;
import uz.asadbek.subcourse.payment.dto.PaymentStatus;
import uz.asadbek.subcourse.payment.dto.PaymentType;

@Getter
@Setter
public class PaymentFilter extends BaseFilter {
    private PaymentStatus status;
    private PaymentType type;
    private LocalDateTime completedAtFrom;
    private LocalDateTime completedAtTo;
    private Long userId;
}
