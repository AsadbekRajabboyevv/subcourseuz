package uz.asadbek.subcourse.balance.topuprequest.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.asadbek.base.dto.BaseAuditResponseDto;

@EqualsAndHashCode(callSuper = true)
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
    private String paymentExId;

    public TopUpRequestResponseDto(
        Long id,
        Long amount,
        String message,
        TopUpStatus status,
        String transactionId,
        Long userId,
        String userFullName,
        String fileKey,
        String comment,
        Long approvedBy,
        String approvedByFullName,
        LocalDateTime approvedAt,
        LocalDateTime rejectedAt,
        String paymentExId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Long createdBy,
        Long updatedBy,
        Long deletedBy
    ) {
        this.id = id;
        this.amount = amount;
        this.message = message;
        this.status = status;
        this.transactionId = transactionId;
        this.userId = userId;
        this.userFullName = userFullName;
        this.fileKey = fileKey;
        this.comment = comment;
        this.approvedBy = approvedBy;
        this.approvedByFullName = approvedByFullName;
        this.approvedAt = approvedAt;
        this.rejectedAt = rejectedAt;
        this.paymentExId = paymentExId;

        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
        this.setDeletedAt(deletedAt);
        this.setCreatedBy(createdBy);
        this.setUpdatedBy(updatedBy);
        this.setDeletedBy(deletedBy);
    }
}
