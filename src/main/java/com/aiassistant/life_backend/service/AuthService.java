package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.dto.LoginRequestDto;
import com.aiassistant.life_backend.dto.LoginResponseDto;
import com.aiassistant.life_backend.dto.RegisterRequestDto;
import com.aiassistant.life_backend.dto.UserResponseDto;

public interface AuthService {
    UserResponseDto register(RegisterRequestDto dto);
    LoginResponseDto login(LoginRequestDto dto);
}
