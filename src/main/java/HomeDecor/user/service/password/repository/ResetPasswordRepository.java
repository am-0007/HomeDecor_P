package HomeDecor.user.service.password.repository;

import HomeDecor.registration.emailConfirmation.EmailConfirmation;
import HomeDecor.user.service.password.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
    Optional<EmailConfirmation> findByToken(String token);

    @Transactional
    @Modifying
    @Query("update tb_reset_password p set " +
            "p.resetAt = ?2 where " +
            "p.token = ?1")
    int updateResetAt(String token, LocalDateTime now);

    @Query("SELECT r from tb_reset_password r where " +
            "r.user.id = ?1 AND r.token = ?2")
    Optional<ResetPassword> findByTokenAndUserID(Long userId, String token);
}
