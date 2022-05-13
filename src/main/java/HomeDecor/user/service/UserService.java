package HomeDecor.user.service;

import HomeDecor.login.LoginRequest;
import HomeDecor.registration.emailConfirmation.ConfirmationTokenService;
import HomeDecor.registration.emailConfirmation.EmailConfirmation;
import HomeDecor.user.User;
import HomeDecor.user.UserRole;
import HomeDecor.user.dto.UserDTO;
import HomeDecor.user.repository.UserInterface;
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

    public Object forgetPassword(Long userId) {
        return null;
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
