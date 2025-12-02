package com.aiassistant.life_backend.dto;

public class LoginResponseDto {
    private UserResponseDto user;
    private String token;

    public LoginResponseDto() {
    }

    public LoginResponseDto(UserResponseDto user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


