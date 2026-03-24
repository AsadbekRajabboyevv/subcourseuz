package uz.asadbek.subcourse.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.asadbek.base.entity.BaseEntity;
import uz.asadbek.base.repository.SoftDeletable;
import uz.asadbek.subcourse.user.dto.UserPositions;


@Getter
@Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends BaseEntity implements SoftDeletable {

    private static final String GENERATOR_NAME = "users_gen";
    private static final String SEQUENCE_NAME = "users_seq";

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = GENERATOR_NAME
    )
    @SequenceGenerator(
        name = GENERATOR_NAME,
        sequenceName = SEQUENCE_NAME,
        allocationSize = 50
    )
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "language", nullable = false)
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private UserPositions position;

    @Column(name = "confirmation_token", unique = true)
    private String confirmationToken;

    @Column(name = "birthDate", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone")
    private String phone;

}
