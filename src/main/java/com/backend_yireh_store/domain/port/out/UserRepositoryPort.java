package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Output Port (hexagonal) - Abstracción del lado infraestructura.
 * DIP: el dominio depende de esta interfaz, no de JPA.
 */
public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    List<User> findAll();
}
