package com.backend_yireh_store.infrastructure.adapter.out.persistence;

import com.backend_yireh_store.domain.model.User;
import com.backend_yireh_store.domain.port.out.UserRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.repository.SpringUserJpaRepository;
import com.backend_yireh_store.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final SpringUserJpaRepository jpaRepository;
    private final UserMapper mapper;

    public UserPersistenceAdapter(SpringUserJpaRepository jpaRepository,
                                  UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public User updateBalance(String email, BigDecimal newBalance) {
        jpaRepository.updateBalance(email, newBalance);
        return jpaRepository.findByEmail(email).map(mapper::toDomain)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + email));
    }

    @Override
    @Transactional
    public User updateProfile(String email, String name, String phone) {
        jpaRepository.updateProfile(email, name, phone);
        return jpaRepository.findByEmail(email).map(mapper::toDomain)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + email));
    }
}