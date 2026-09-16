package com.backend_yireh_store.infrastructure.adapter.out.persistence.repository;

import com.backend_yireh_store.domain.model.OrderStatus;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserEmailOrderByCreatedAtDesc(String userEmail);
    List<OrderEntity> findByStatusOrderByCreatedAtDesc(OrderStatus status);
    List<OrderEntity> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(o) + 1 FROM OrderEntity o")
    long countPlusOne();
}
