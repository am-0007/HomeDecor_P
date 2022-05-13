package HomeDecor.user.service.password;

import HomeDecor.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "tb_reset_password")
public class ResetPassword {

    @Id
    @SequenceGenerator(
            name = "reset_Password_seq",
            sequenceName = "reset_Password_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reset_Password_seq"
    )
    private Long id;

    private String token;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime resetAt;

    public ResetPassword(User user,
                         String token,
                         LocalDateTime createdAt,
                         LocalDateTime expiresAt) {
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.token = token;
    }
}
