package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.User;
import com.backend_yireh_store.domain.port.in.AuthUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.AuthResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.LoginRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.RegisterRequest;
import com.backend_yireh_store.infrastructure.config.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final JwtService jwtService;

    public AuthController(AuthUseCase authUseCase, JwtService jwtService) {
        this.authUseCase = authUseCase;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        User user = authUseCase.register(req.name(), req.phone(), req.email(), req.password());
        String token = generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.fromUser(user, token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        User user = authUseCase.login(req.email(), req.password());
        String token = generateToken(user);
        return ResponseEntity.ok(AuthResponse.fromUser(user, token));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Yireh Store API OK");
    }

    private String generateToken(User user) {
        java.util.Set<String> roleNames = user.getRoles() != null
                ? user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
                : java.util.Set.of();
        return jwtService.generateToken(user.getId(), user.getEmail(), roleNames);
    }
}
