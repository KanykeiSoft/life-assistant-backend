package com.aiassistant.life_backend.controller;


import com.aiassistant.life_backend.dto.LoginRequestDto;
import com.aiassistant.life_backend.dto.LoginResponseDto;
import com.aiassistant.life_backend.dto.RegisterRequestDto;
import com.aiassistant.life_backend.dto.UserResponseDto;
import com.aiassistant.life_backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // чтобы Flutter/веб мог ходить к бэку
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register (@RequestBody RegisterRequestDto dto){
        UserResponseDto user = authService.register(dto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto){
        LoginResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
