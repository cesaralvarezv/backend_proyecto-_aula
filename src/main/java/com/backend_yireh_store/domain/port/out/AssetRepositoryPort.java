package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.PhysicalAsset;

import java.util.List;
import java.util.Optional;

public interface AssetRepositoryPort {

    PhysicalAsset save(PhysicalAsset asset);

    Optional<PhysicalAsset> findById(Long id);

    Optional<PhysicalAsset> findByAssetCode(String assetCode);

    List<PhysicalAsset> findAll();

    void deleteById(Long id);

    boolean existsByAssetCode(String assetCode);
}
