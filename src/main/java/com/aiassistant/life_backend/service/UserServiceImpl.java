package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.dto.UserResponseDto;
import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
  private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto getMe(String email) {
       User user = userRepository.findByEmail(email)
               .orElseThrow(()-> new RuntimeException("User not found"));

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setTimezone(user.getTimezone());

        return dto;
    }

}
