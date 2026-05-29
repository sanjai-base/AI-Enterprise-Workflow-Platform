package com.workflow.auth.controller;

import com.workflow.auth.domain.User;
import com.workflow.auth.dto.AuthRequest;
import com.workflow.auth.dto.AuthResponse;
import com.workflow.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*") // For local dev
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            String token = authService.authenticate(request.getEmail(), request.getPassword());
            User user = authService.getUserByEmail(request.getEmail());
            
            return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getRole().name(), user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }
    }
}
