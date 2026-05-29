package com.workflow.auth.dto;

import java.util.UUID;

public class AuthResponse {
    private String token;
    private UUID userId;
    private String role;
    private String email;

    public AuthResponse(String token, UUID userId, String role, String email) {
        this.token = token;
        this.userId = userId;
        this.role = role;
        this.email = email;
    }

    public String getToken() { return token; }
    public UUID getUserId() { return userId; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
}
