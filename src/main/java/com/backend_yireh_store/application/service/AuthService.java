package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.exception.InvalidCredentialsException;
import com.backend_yireh_store.domain.exception.UserAlreadyExistsException;
import com.backend_yireh_store.domain.model.User;
import com.backend_yireh_store.domain.port.in.AuthUseCase;
import com.backend_yireh_store.domain.port.out.PasswordEncoderPort;
import com.backend_yireh_store.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service - Implementa caso de uso, depende solo de puertos (DIP).
 * SRP: solo lógica de autenticación, no conoce JPA ni HTTP.
 */
@Service
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public AuthService(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(String name, String phone, String email, String password) {
        // Validaciones de aplicación (además del dominio)
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email requerido");
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Contraseña mínimo 6 caracteres");
        if (userRepository.existsByEmail(email.toLowerCase().trim())) {
            throw new UserAlreadyExistsException(email);
        }
        String hash = passwordEncoder.encode(password);
        User user = User.createNew(name, phone, email, hash);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new InvalidCredentialsException());
    }
}
