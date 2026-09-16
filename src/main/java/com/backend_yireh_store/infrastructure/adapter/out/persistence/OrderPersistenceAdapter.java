package com.backend_yireh_store.infrastructure.adapter.out.persistence;

import com.backend_yireh_store.domain.model.Order;
import com.backend_yireh_store.domain.model.OrderStatus;
import com.backend_yireh_store.domain.port.out.OrderRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.repository.OrderJpaRepository;
import com.backend_yireh_store.infrastructure.mapper.OrderMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository jpaRepository;
    private final OrderMapper mapper;

    public OrderPersistenceAdapter(OrderJpaRepository jpaRepository, OrderMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(order)));
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Order> findByUserEmail(String userEmail) {
        return jpaRepository.findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return jpaRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public String generateOrderNumber() {
        long seq = jpaRepository.countPlusOne();
        return String.format("ORD-%s-%04d", LocalDate.now().toString().replace("-", ""), seq);
    }
}
