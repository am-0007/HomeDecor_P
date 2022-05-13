package HomeDecor.user.controller;


import HomeDecor.user.UserRole;
import HomeDecor.user.service.UserService;
import HomeDecor.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //SUPER_ADMIN
    //getAllUser
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    //get user using pagination
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/getUserByPagination/{offset}/{pageSize}")
    public ResponseEntity<?> getUserUsingPagination(@PathVariable("offset") Integer offset, @PathVariable("pageSize") Integer pageSize) {
        return new ResponseEntity<>(userService.getUserUsingPagination(offset, pageSize), HttpStatus.OK);
    }

    //getUserByRole
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/getUserByRole/{userRole}")
    public ResponseEntity<List<UserDTO>> getUserByRole(@PathVariable("userRole")UserRole userRole) {
        return new ResponseEntity<>(userService.getUserByRole(userRole), HttpStatus.OK);
    }

    //return Total number of user
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/getTotalUser")
    public ResponseEntity<?> getTotalUser() {
        return new ResponseEntity<>(userService.getTotalUser(), HttpStatus.OK);
    }

    //Remove User
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/removeUser/{userId}")
    public ResponseEntity<?> removeUser(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.removeUser(userId), HttpStatus.OK);
    }

    //forgetPassword
    @PostMapping("/forgetPassword/{userId}")
    public ResponseEntity<?> forgetPassword(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.forgetPassword(userId), HttpStatus.OK);
    }

    //changePassword
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN', 'ROLE_USER')")
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam("oldPassword")String oldPassword,
                                            @RequestParam("newPassword") String newPassword,
                                            Principal principal) {
        try {
            return new ResponseEntity<>(userService.changePassword(oldPassword, newPassword, principal), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Your old password is invalid!");
        }
    }
}


