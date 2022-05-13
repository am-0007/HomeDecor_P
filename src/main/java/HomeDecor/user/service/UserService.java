package HomeDecor.user.service;

import HomeDecor.login.LoginRequest;
import HomeDecor.registration.emailConfirmation.ConfirmationTokenService;
import HomeDecor.registration.emailConfirmation.EmailConfirmation;
import HomeDecor.registration.emailConfirmation.email.EmailSender;
import HomeDecor.user.User;
import HomeDecor.user.UserRole;
import HomeDecor.user.dto.UserDTO;
import HomeDecor.user.repository.UserInterface;
import HomeDecor.user.service.password.ResetPassword;
import HomeDecor.user.service.password.service.PasswordResetService;
import org.apache.hadoop.yarn.webapp.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static HomeDecor.user.UserRole.*;
@Service
public class UserService implements UserDetailsService {

    private final UserInterface userInterface;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordResetService passwordResetService;
    private final EmailSender emailSender;

    @Autowired
    public UserService(UserInterface userInterface,
                       PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService, PasswordResetService passwordResetService, EmailSender emailSender) {
        this.userInterface = userInterface;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.passwordResetService = passwordResetService;
        this.emailSender = emailSender;
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


    //SuperAdmin
    public Object removeUser(Long userId) {
        Optional<User> userExists = userInterface.findById(userId);
        if (userExists.isEmpty()) {
            throw new NotFoundException("userId: " + userId + ", you are seaching is not available");
        } else {
            User user = userExists.get();
            userInterface.delete(user);
            return HttpStatus.OK;
        }
    }

    //getAllUser
    public List<UserDTO> getAllUser() {
        List<User> users = userInterface.findAll();
        List<UserDTO> userResponse = new ArrayList<>();
        for (User u : users) {
            if (u.getUserRole() != SUPERADMIN) {
                userResponse.add(new UserDTO(
                        u.getId(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getEmail(),
                        u.getDateOfBirth(),
                        u.getPhoneNumber(),
                        u.getUsername(),
                        u.getEnabled(),
                        !u.getLocked(),
                        u.getGender().toString(),
                        u.getUserRole().toString()
                ));
            }
        }
        return userResponse;
    }

    public List<UserDTO> getUserByRole(UserRole userRole) {
        List<User> users = userInterface.findAllByUserRole(userRole);
        List<UserDTO> userResponse = new ArrayList<>();
        for (User u : users) {
            userResponse.add(new UserDTO(
                    u.getId(),
                    u.getFirstName(),
                    u.getLastName(),
                    u.getEmail(),
                    u.getDateOfBirth(),
                    u.getPhoneNumber(),
                    u.getUsername(),
                    u.getEnabled(),
                    !u.getLocked(),
                    u.getGender().toString(),
                    u.getUserRole().toString()
            ));
        }
        return userResponse;
    }

    public Object getTotalUser() {
        return userInterface.countUser();
    }

    public List<UserDTO> getUserUsingPagination(Integer offset, Integer pageSize) {
        Page<User> fetchUser = userInterface.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by("id")));
        List<UserDTO> fetchUsers = new ArrayList<>();
        for (User u : fetchUser) {
            fetchUsers.add(new UserDTO(
                    u.getId(),
                    u.getFirstName(),
                    u.getLastName(),
                    u.getEmail(),
                    u.getDateOfBirth(),
                    u.getPhoneNumber(),
                    u.getUsername(),
                    u.getEnabled(),
                    !u.getLocked(),
                    u.getGender().toString(),
                    u.getUserRole().toString()
            ));
        }
        return fetchUsers;
    }

    //reset password
    public Object forgetPassword(String username, String email) throws Exception{
        Optional<User> userExists = userInterface.findByUsernameAndEmail(username, email);
        if (userExists.isPresent()) {
            String token = passwordResetService.generateToken();
            User user = userExists.get();
            ResetPassword resetPassword = new ResetPassword(
                    user,
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15)
            );
            passwordResetService.saveResetPasswordRequest(resetPassword);
            emailSender.send(
                    email,
                    buildResetPasswordEmail(user.getFirstName(), token)
            );
            return "We have send token in your mail to reset your password. Thank You!";
        } else {
            throw new Exception("Incorrect details");
        }
    }

    public String resetPassword(String username, String token, String newPassword) throws Exception {
        Optional<User> userExists = userInterface.findByUsername(username);
        if (userExists.isPresent()) {
            User user = userExists.get();
            ResetPassword resetPassword = passwordResetService
                    .getToken(token, user.getId())
                    .orElseThrow(
                            () -> new IllegalStateException("Invalid Token!"));
            if (resetPassword.getResetAt() != null) {
                throw new IllegalStateException("Password already reset.");
            }

            LocalDateTime resetAt = resetPassword.getExpiresAt();
            if (resetAt.isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("token expired");
            }
            passwordResetService.setResetAt(token);
            user.setPassword(passwordEncoder.encode(newPassword));
            userInterface.save(user);
            return "Password Successfully reset.";

        } else {
            return "User does not exists";
        }
    }

    private String buildResetPasswordEmail(String firstName, String token) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"prewhat is mime message in javasentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + firstName + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Please enter below token to reset password. </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Token: " + token + "</p></blockquote>\n Link will expire in 5 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public Object changePassword(String oldPassword,
                                 String newPassword,
                                 Principal principal) throws Exception{

        String username = principal.getName();
        Optional<User> userExists = userInterface.findByUsername(username);
        if (userExists.isPresent()) {
            User user = userExists.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userInterface.save(user);
                return "Your password is changed...";
            }
        }
        return new Exception("Sorry, Your previous password doesn't match...");
    }
}
