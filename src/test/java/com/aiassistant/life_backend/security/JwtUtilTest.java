package com.aiassistant.life_backend.security;

import com.aiassistant.life_backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        String base64Secret =
                "dGVzdC1zZWNyZXQtdGVzdC1zZWNyZXQtdGVzdC1zZWNyZXQtdGVzdA==";

        jwtUtil = new JwtUtil(
                base64Secret,
                60L,              // ttlMinutes
                "life-assistant", // issuer
                "life-users"      // audience
        );
    }

    @Test
    void generateToken_shouldCreateJwt() {
        User user = new User();
        user.setEmail("test@test.com");

        String token = jwtUtil.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals(3, token.split("\\.").length); // header.payload.signature
    }

    @Test
    void validateToken_shouldReturnTrue_forValidToken() {
        User user = new User();
        user.setEmail("test@test.com");

        String token = jwtUtil.generateToken(user);

        assertTrue(jwtUtil.validateToken(token)); // ✅ 1 аргумент
    }

    @Test
    void validateToken_shouldReturnFalse_forBrokenToken() {
        assertFalse(jwtUtil.validateToken("abc.def.ghi"));
    }

}
