package com.aiassistant.life_backend.dto;

public class UserResponseDto {
    public Long id;
    public String name;
    public String email;
    public String timezone;


    public UserResponseDto() {
        // пустой конструктор обязателен для Spring (JSON → Object)
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
