package HomeDecor.registration.emailConfirmation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository
        extends JpaRepository<EmailConfirmation, Long> {

    Optional<EmailConfirmation> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE tb_email_confirmation e " +
            "SET e.confirmAt = ?2 " +
            "WHERE e.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmAt);
}
