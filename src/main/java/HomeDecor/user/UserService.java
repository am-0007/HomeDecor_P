package HomeDecor.user;

import HomeDecor.login.LoginRequest;
import HomeDecor.registration.emailConfirmation.ConfirmationTokenService;
import HomeDecor.registration.emailConfirmation.EmailConfirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserInterface userInterface;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public UserService(UserInterface userInterface,
                       PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService) {
        this.userInterface = userInterface;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userInterface
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("Username %s not found", username))
                );
    }

    public String signUpUser(User user) {
        boolean userExists = userInterface.findByUsername(user.getUsername()).isPresent();
        boolean emailExists = userInterface.findByEmail(user.getEmail()).isPresent();
        if (userExists) {
            return ("Username already Taken");
        } else if (emailExists) {
            return ("Email exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userInterface.save(user);

        String token = UUID.randomUUID().toString();
        EmailConfirmation confirmationToken = new EmailConfirmation(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableUser(String user) {
        return userInterface.enableUser(user);
    }

    public String loginUser(LoginRequest loginRequest) {
        boolean usernameExists = userInterface.findByUsername(loginRequest.getUsername()).isPresent();
        System.out.println(usernameExists);
        if (!usernameExists) {
            throw new IllegalStateException("Username does not exists!");
        }
        Optional<User> userOptional = userInterface.findByUsername(loginRequest.getUsername());
        if (userOptional.isPresent()) {
            if (userOptional.get().getPassword().equals(loginRequest.getPassword())) {
                return "login";
            }
        }
        return "Password error!!";
    }





}
