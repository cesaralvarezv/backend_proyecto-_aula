package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.PhysicalAsset;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.AssetRequest;

import java.util.List;

public interface AssetUseCase {

    PhysicalAsset createAsset(AssetRequest request);

    PhysicalAsset updateAsset(Long id, AssetRequest request);

    PhysicalAsset getById(Long id);

    List<PhysicalAsset> getAllAssets();

    void deleteAsset(Long id);
}
