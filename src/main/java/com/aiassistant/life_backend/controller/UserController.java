package com.aiassistant.life_backend.controller;

import com.aiassistant.life_backend.dto.UserResponseDto;
import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.repository.UserRepository;
import com.aiassistant.life_backend.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // CREATE
    @GetMapping("/me")
    public UserResponseDto me(Authentication authentication) {
        String email = authentication.getName();
       return userService.getMe(email);
    }

}
