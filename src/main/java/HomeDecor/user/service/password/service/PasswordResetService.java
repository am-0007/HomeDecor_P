package HomeDecor.user.service.password.service;

import HomeDecor.registration.emailConfirmation.EmailConfirmation;
import HomeDecor.user.User;
import HomeDecor.user.service.password.ResetPassword;
import HomeDecor.user.service.password.repository.ResetPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class PasswordResetService {

    private final ResetPasswordRepository resetPasswordRepository;

    @Autowired
    public PasswordResetService(ResetPasswordRepository resetPasswordRepository) {
        this.resetPasswordRepository = resetPasswordRepository;
    }

    public String generateToken() {
        int leftLimit = 48;
        int rightLimit = 122;
        int stringLength = 8;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i>= 97))
                .limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public Optional<ResetPassword> getToken(String token, Long userId) {
        return resetPasswordRepository.findByTokenAndUserID(userId, token);
    }

    public void saveResetPasswordRequest(ResetPassword resetPassword) {
        resetPasswordRepository.save(resetPassword);
    }

    public int setResetAt(String token) {
        return resetPasswordRepository.updateResetAt(token, LocalDateTime.now());
    }
}
