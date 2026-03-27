package uz.asadbek.subcourse.balance.topuprequest.dto;

import java.time.LocalDateTime;
import lombok.Data;
import uz.asadbek.base.dto.BaseAuditResponseDto;

@Data
public class TopUpRequestResponseDto extends BaseAuditResponseDto {
    private Long id;
    private Long amount;
    private String message;
    private TopUpStatus status;
    private String transactionId;
    private Long userId;
    private String userFullName;
    private String fileKey;
    private String comment;
    private Long approvedBy;
    private String approvedByFullName;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private Long paymentId;
}
