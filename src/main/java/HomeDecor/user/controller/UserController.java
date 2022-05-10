package HomeDecor.user.controller;


import HomeDecor.user.User;
import HomeDecor.user.UserRole;
import HomeDecor.user.UserService;
import HomeDecor.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/superAdmin")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //getAllUser
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
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



}
