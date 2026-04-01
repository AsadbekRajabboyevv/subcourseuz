package uz.asadbek.subcourse.util.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserPurchaseId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "purchased_at")
    private LocalDateTime purchasedAt = LocalDateTime.now();
}
