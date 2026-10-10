package com.backend_yireh_store.infrastructure.adapter.out.persistence;

import com.backend_yireh_store.domain.model.PhysicalAsset;
import com.backend_yireh_store.domain.port.out.AssetRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.PhysicalAssetEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.repository.PhysicalAssetJpaRepository;
import com.backend_yireh_store.infrastructure.mapper.PhysicalAssetMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PhysicalAssetPersistenceAdapter implements AssetRepositoryPort {

    private final PhysicalAssetJpaRepository jpaRepository;
    private final PhysicalAssetMapper mapper;

    public PhysicalAssetPersistenceAdapter(PhysicalAssetJpaRepository jpaRepository, PhysicalAssetMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PhysicalAsset save(PhysicalAsset asset) {
        PhysicalAssetEntity entity = mapper.toEntity(asset);
        PhysicalAssetEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<PhysicalAsset> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<PhysicalAsset> findByAssetCode(String assetCode) {
        return jpaRepository.findByAssetCode(assetCode).map(mapper::toDomain);
    }

    @Override
    public List<PhysicalAsset> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByAssetCode(String assetCode) {
        return jpaRepository.existsByAssetCode(assetCode);
    }
}
