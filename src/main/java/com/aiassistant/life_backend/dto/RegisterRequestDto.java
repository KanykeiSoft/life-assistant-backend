package com.aiassistant.life_backend.dto;

public class RegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private String timezone;

    public RegisterRequestDto() {
        // нужен пустой конструктор для сериализации JSON → объект
    }

    public RegisterRequestDto(String name, String email, String password, String timezone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.timezone = timezone;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}

