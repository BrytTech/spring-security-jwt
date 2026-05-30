package org.bright.springsecurity.controller;


import org.bright.springsecurity.dto.AuthRequest;
import org.bright.springsecurity.entity.Role;
import org.bright.springsecurity.service.AuthService;
import org.bright.springsecurity.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/encodePassword")
    public void saveUserWithEncodedPassword(@RequestParam String username,
                                            @RequestParam String password,
                                            @RequestParam Role role){

        userService.saveUserWithEncodedPassword(username, password, role);
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthRequest request){
        return authService.authenticate(request.username(), request.password());
    }

}
