package com.placementhub.backend.dto;

public class LoginResponse {
    private String token;
    private String role;
    private Long id;
    private String email;
    private String name;

    public LoginResponse() {}

    public LoginResponse(String token, String role, Long id, String email, String name) {
        this.token = token;
        this.role = role;
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
