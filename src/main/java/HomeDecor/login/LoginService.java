package HomeDecor.login;

import HomeDecor.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {
    private final UserService userService;

    public String login(LoginRequest loginRequest){
        System.out.println(loginRequest.getUsername());
        System.out.println(loginRequest.getPassword());
        String login = userService.loginUser(
                new LoginRequest(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        return "{" + "message :" + login + "}";
    }
}
