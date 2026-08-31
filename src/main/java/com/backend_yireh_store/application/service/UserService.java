package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.exception.InvalidCredentialsException;
import com.backend_yireh_store.domain.exception.UserAlreadyExistsException;
import com.backend_yireh_store.domain.model.RoleName;
import com.backend_yireh_store.domain.model.User;
import com.backend_yireh_store.domain.port.in.UserUseCase;
import com.backend_yireh_store.domain.port.out.PasswordEncoderPort;
import com.backend_yireh_store.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;

@Service
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public UserService(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createAdmin(String name, String phone, String email, String password, String role) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email requerido");
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Contrasena minimo 6 caracteres");
        if (userRepository.existsByEmail(email.toLowerCase().trim())) {
            throw new UserAlreadyExistsException(email);
        }
        RoleName roleName;
        try {
            roleName = RoleName.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol invalido: " + role);
        }
        String hash = passwordEncoder.encode(password);
        User user = new User(null, name.trim(), phone.trim(), email.trim().toLowerCase(),
                hash, EnumSet.of(roleName, RoleName.USER),
                java.time.LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        // No devolver passwords - el controller se encarga de filtrar
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new InvalidCredentialsException());
    }
}
