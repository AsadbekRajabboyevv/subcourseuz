package uz.asadbek.subcourse.balance.topuprequest.filter;

import lombok.Data;
import uz.asadbek.base.filter.BaseFilter;
import uz.asadbek.subcourse.balance.topuprequest.dto.TopUpStatus;

@Data
public class TopUpRequestFilter extends BaseFilter {

    private String transactionId;
    private String message;
    private TopUpStatus status;
    private Long amountTo;
    private Long amountFrom;
}
