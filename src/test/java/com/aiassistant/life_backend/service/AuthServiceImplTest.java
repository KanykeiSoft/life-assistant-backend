package com.aiassistant.life_backend.service;

import com.aiassistant.life_backend.dto.LoginRequestDto;
import com.aiassistant.life_backend.dto.RegisterRequestDto;
import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.repository.UserRepository;
import com.aiassistant.life_backend.security.JwtUtil;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private AuthServiceImpl authService;

    @Test
    void register_shouldSaveUser_withEncodedPassword() {
        // given
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setName("Test");
        dto.setEmail("test@test.com");
        dto.setPassword("123456");
        dto.setTimezone("EST");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("ENC_PASS");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L); // имитируем, что БД присвоила id
            return u;
        });

        // when
        authService.register(dto);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("Test", saved.getName());
        assertEquals("test@test.com", saved.getEmail());
        assertEquals("ENC_PASS", saved.getPasswordHash());
        assertEquals("EST", saved.getTimezone());

        verify(passwordEncoder).encode("123456");
    }


    @Test
    void register_shouldThrow_ifEmailAlreadyExists() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@test.com");
        dto.setPassword("123456");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(dto));
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void login_shouldReturnToken_whenPasswordMatches() {
        // given
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("test@test.com");
        dto.setPassword("123456");

        User user = new User();
        user.setId(1L);
        user.setEmail(dto.getEmail());
        user.setPasswordHash("ENC_PASS"); // <-- ВАЖНО

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn("JWT_TOKEN");

        // when
        var resp = authService.login(dto);

        // then
        assertNotNull(resp);
        assertEquals("JWT_TOKEN", resp.getToken());

        verify(jwtUtil).generateToken(user);
    }

    @Test
    void login_shouldThrow_ifUserNotFound() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("missing@test.com");
        dto.setPassword("123456");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(dto));
        verify(jwtUtil, never()).generateToken(any(User.class)); // <-- скобки и ; на месте
    }

    @Test
    void login_shouldThrow_ifPasswordWrong() {
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("test@test.com");
        dto.setPassword("wrong");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash("ENC_PASS"); // <-- ВАЖНО

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(dto));
        verify(jwtUtil, never()).generateToken(any(User.class)); // <-- скобки и ; на месте
    }
}