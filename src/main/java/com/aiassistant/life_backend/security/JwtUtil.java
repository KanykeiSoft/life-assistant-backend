package com.aiassistant.life_backend.security;

import com.aiassistant.life_backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;           // секретный ключ для подписи
    private final long ttlMinutes;   // время жизни токена в минутах
    private final String issuer;     // кто выдал
    private final String audience;   // для кого

    private static final long CLOCK_SKEW_SECONDS = 60; // допустимое расхождение часов

    public JwtUtil(
            @Value("${jwt.secret}") String base64Secret,
            @Value("${jwt.ttl-minutes:60}") long ttlMinutes,
            @Value("${jwt.issuer:life-assistant}") String issuer,
            @Value("${jwt.audience:life-users}") String audience
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.ttlMinutes = ttlMinutes;
        this.issuer = issuer;
        this.audience = audience;
    }

    // ---------- генерация токена ----------

    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(ttlMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId())) // в subject кладём userId
                .setIssuer(issuer)
                .setAudience(audience)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ---------- валидация токена ----------

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .requireAudience(audience)
                    .setAllowedClockSkewSeconds(CLOCK_SKEW_SECONDS)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // можно залогировать, если захочешь
            return false;
        }
    }

    // ---------- вытащить userId из токена ----------

    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setAllowedClockSkewSeconds(CLOCK_SKEW_SECONDS)
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String subject = claims.getSubject(); // мы туда положили userId в виде строки
        return Long.valueOf(subject);
    }
}

