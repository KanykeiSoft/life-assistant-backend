package com.aiassistant.life_backend.security;

import com.aiassistant.life_backend.model.User;
import com.aiassistant.life_backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.io.IOException;
import java.util.Collections;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // DEBUG (можно оставить на время)
            System.out.println("JWT FILTER PATH = " + request.getServletPath());
            System.out.println("AUTH HEADER = " + request.getHeader("Authorization"));

            // 1. Читаем заголовок Authorization
            String header = request.getHeader("Authorization");

            // Если токена нет или формат неправильный — пропускаем дальше
            if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. Извлекаем токен (Bearer <token>)
            String token = header.substring(7);

            // 3. Проверяем токен
            if (!jwtUtil.validateToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 4. Достаём userId из токена
            Long userId = jwtUtil.getUserId(token);

            // 5. Ищем пользователя в БД
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 6. Создаём UserDetails
            UserDetails principal =
                    org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPasswordHash())
                            .authorities("ROLE_USER")
                            .build();

            // 7. Authentication
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            // 8. Кладём в SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("AUTH SET = " + SecurityContextHolder.getContext().getAuthentication());
            System.out.println("PATH DONE = " + request.getServletPath());


            // 9. Продолжаем цепочку
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // 🔥 ВАЖНО: не ломаем запрос
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        System.out.println("BEFORE CHAIN AUTH = " + SecurityContextHolder.getContext().getAuthentication());

        return request.getServletPath().startsWith("/api/auth/");
    }
}



