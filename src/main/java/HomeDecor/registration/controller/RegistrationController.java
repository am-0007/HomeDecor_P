package HomeDecor.registration.controller;

import HomeDecor.login.JWT.token.JWTTokenHelper;
import HomeDecor.login.LoginRequest;
import HomeDecor.login.LoginResponse;
import HomeDecor.login.LoginService;
import HomeDecor.login.UserInfo;
import HomeDecor.registration.dto.RegistrationDTO;
import HomeDecor.registration.service.RegistrationService;
import HomeDecor.user.User;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import static HomeDecor.user.UserRole.*;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final LoginService loginService;

    private AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JWTTokenHelper jwtTokenHelper;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    //Normal User
    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody RegistrationDTO request) {
        request.setUserRole(USER);
        return new ResponseEntity<>(registrationService.register(request), HttpStatus.OK);
    }

    //seller as AdminUser
    @PostMapping("/adminRegistration")
    public ResponseEntity<?> adminRegister(@RequestBody RegistrationDTO request) {
        request.setUserRole(ADMIN);
        return new ResponseEntity<>(registrationService.register(request), HttpStatus.OK);
    }

    //SuperAdmin
    @PostMapping("/superAdminRegistration")
    public ResponseEntity<?> superAdminRegister(@RequestBody RegistrationDTO request) {
        request.setUserRole(SUPERADMIN);
        return new ResponseEntity<>(registrationService.register(request), HttpStatus.OK);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }


    //NormalUserLogin
    @SneakyThrows
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User)authentication.getPrincipal();

        String jwtToken = jwtTokenHelper.generateToken(user.getUsername());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setUsername(user.getUsername());
        loginResponse.setId(user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    //AdminUserLogin
    @SneakyThrows
    @PostMapping("/auth/adminLogin")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User)authentication.getPrincipal();

        String jwtToken = jwtTokenHelper.generateToken(user.getUsername());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setUsername(user.getUsername());
        loginResponse.setId(user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    //SuperAdmin
    @SneakyThrows
    @PostMapping("/auth/superAdminLogin")
    public ResponseEntity<?> superAdminLogin(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtTokenHelper.generateToken(user.getUsername()));
        loginResponse.setUsername(user.getUsername());
        loginResponse.setId(user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @GetMapping("/auth/userinfo")
    public ResponseEntity<?> getUserInfo(Principal user) {
        User userObj = (User) userDetailsService.loadUserByUsername(user.getName());

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userObj.getUsername());

        return ResponseEntity.ok(userInfo);
    }
}
