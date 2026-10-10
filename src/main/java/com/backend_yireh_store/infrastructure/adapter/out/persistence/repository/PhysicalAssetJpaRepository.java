package com.backend_yireh_store.infrastructure.adapter.out.persistence.repository;

import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.PhysicalAssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhysicalAssetJpaRepository extends JpaRepository<PhysicalAssetEntity, Long> {

    Optional<PhysicalAssetEntity> findByAssetCode(String assetCode);

    boolean existsByAssetCode(String assetCode);
}
