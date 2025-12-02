package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.dto.LoginRequestDto;
import com.aiassistant.life_backend.dto.LoginResponseDto;
import com.aiassistant.life_backend.dto.RegisterRequestDto;
import com.aiassistant.life_backend.dto.UserResponseDto;
import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto register(RegisterRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setTimezone(dto.getTimezone());

        // saved in db
        User saved = userRepository.save(user);

        // return dto
        return toUserResponseDto(saved);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
       User user = userRepository.findByEmail(dto.getEmail())
               .orElseThrow(()-> new RuntimeException("User not found"));
       if(!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())){
           throw new RuntimeException("Invalid email or password");
       }
        String fakeToken = "dummy-token-user-" + user.getId();

        LoginResponseDto response = new LoginResponseDto();
        response.setUser(toUserResponseDto(user));
        response.setToken(fakeToken);

        return response;
    }

    // ---------- Mapping User → UserResponseDto ----------
    private UserResponseDto toUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setTimezone(user.getTimezone());
        return dto;
    }

}
