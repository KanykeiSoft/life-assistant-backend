package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.dto.UserResponseDto;

public interface UserService {
    UserResponseDto getMe(String email);
}
