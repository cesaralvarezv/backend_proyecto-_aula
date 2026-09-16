package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.User;
import com.backend_yireh_store.domain.port.in.UserUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CreateUserRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.UpdateProfileRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> users = userUseCase.getAll().stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.fromUser(userUseCase.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest req) {
        User user = userUseCase.createAdmin(req.name(), req.phone(), req.email(), req.password(), req.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(user));
    }

    /** GET /api/users/me — perfil del usuario autenticado. */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getMyProfile(Authentication auth) {
        return ResponseEntity.ok(UserResponse.fromUser(userUseCase.getProfile(auth.getName())));
    }

    /** PUT /api/users/me — actualizar nombre y teléfono del usuario autenticado. */
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateMyProfile(Authentication auth,
                                                         @RequestBody UpdateProfileRequest req) {
        User updated = userUseCase.updateProfile(auth.getName(), req.name(), req.phone());
        return ResponseEntity.ok(UserResponse.fromUser(updated));
    }
}

