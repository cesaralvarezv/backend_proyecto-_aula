package com.backend_yireh_store.infrastructure.adapter.out.persistence.repository;

import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Adapter secundario Spring Data JPA - solo vive en infraestructura.
 */
public interface SpringUserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE UserEntity u SET u.balance = :balance WHERE u.email = :email")
    int updateBalance(@Param("email") String email, @Param("balance") BigDecimal balance);

    @Modifying
    @Query("UPDATE UserEntity u SET u.name = :name, u.phone = :phone WHERE u.email = :email")
    int updateProfile(@Param("email") String email, @Param("name") String name, @Param("phone") String phone);
}

