package com.backend_yireh_store.infrastructure.adapter.out.persistence.repository;

import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByNameIgnoreCase(String name);
    List<ProductEntity> findByActiveTrue();
    List<ProductEntity> findByCategoryId(Long categoryId);
    List<ProductEntity> findByCategoryIdAndActiveTrue(Long categoryId);
    
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<ProductEntity> searchByName(@Param("query") String query);
    
    @Query("SELECT p FROM ProductEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) AND p.category.id = :categoryId")
    List<ProductEntity> searchByNameAndCategory(@Param("query") String query, @Param("categoryId") Long categoryId);
    
    boolean existsByNameIgnoreCase(String name);
}