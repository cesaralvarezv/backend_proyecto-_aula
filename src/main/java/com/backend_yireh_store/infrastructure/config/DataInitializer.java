package com.backend_yireh_store.infrastructure.config;

import com.backend_yireh_store.domain.model.RoleName;
import com.backend_yireh_store.domain.model.User;
import com.backend_yireh_store.domain.port.out.PasswordEncoderPort;
import com.backend_yireh_store.domain.port.out.UserRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * Crea el usuario ADMIN inicial al arrancar la aplicación.
 * Lee credenciales de .env / application.properties.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    @Value("${admin.email:admin@yirehstore.com}")
    private String adminEmail;

    @Value("${admin.password:Admin123456}")
    private String adminPassword;

    @Value("${admin.name:Administrador}")
    private String adminName;

    public DataInitializer(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail(adminEmail)) {
            String hash = passwordEncoder.encode(adminPassword);
            User admin = new User(null, adminName, "0000000000", adminEmail, hash,
                    EnumSet.of(RoleName.ADMIN, RoleName.USER),
                    java.time.LocalDateTime.now());
            userRepository.save(admin);
            log.info("[DataInitializer] Usuario ADMIN creado: {}", adminEmail);
        } else {
            log.info("[DataInitializer] Usuario ADMIN ya existe: {}", adminEmail);
        }
    }
}
