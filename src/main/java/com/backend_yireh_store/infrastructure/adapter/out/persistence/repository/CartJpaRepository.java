package com.backend_yireh_store.infrastructure.adapter.out.persistence.repository;

import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByUserId(String userId);
}