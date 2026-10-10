package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.PhysicalAsset;
import com.backend_yireh_store.domain.port.in.AssetUseCase;
import com.backend_yireh_store.domain.port.out.AssetRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.AssetRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AssetService implements AssetUseCase {

    private final AssetRepositoryPort assetRepository;

    public AssetService(AssetRepositoryPort assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public PhysicalAsset createAsset(AssetRequest request) {
        if (assetRepository.existsByAssetCode(request.getAssetCode())) {
            throw new IllegalArgumentException("Ya existe un activo físico con la placa/código: " + request.getAssetCode());
        }

        PhysicalAsset asset = PhysicalAsset.createNew(
                request.getAssetCode(),
                request.getName(),
                request.getCategory(),
                request.getLocation(),
                request.getPurchasePrice(),
                request.getPurchaseDate(),
                request.getUsefulLifeMonths() != null ? request.getUsefulLifeMonths() : 60,
                request.getMaintenanceSchedule(),
                request.getNextMaintenanceDate(),
                request.getNotes()
        );

        return assetRepository.save(asset);
    }

    @Override
    public PhysicalAsset updateAsset(Long id, AssetRequest request) {
        PhysicalAsset asset = assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activo físico no encontrado con ID: " + id));

        // Si cambia el assetCode, validar que no esté ocupado
        if (!asset.getAssetCode().equalsIgnoreCase(request.getAssetCode()) &&
                assetRepository.existsByAssetCode(request.getAssetCode())) {
            throw new IllegalArgumentException("La placa " + request.getAssetCode() + " ya está en uso.");
        }

        asset.setAssetCode(request.getAssetCode().trim().toUpperCase());
        asset.setName(request.getName().trim());
        asset.setCategory(request.getCategory().trim());
        asset.setLocation(request.getLocation() != null ? request.getLocation().trim() : "");
        asset.setPurchasePrice(request.getPurchasePrice());
        if (request.getPurchaseDate() != null) asset.setPurchaseDate(request.getPurchaseDate());
        if (request.getUsefulLifeMonths() != null) asset.setUsefulLifeMonths(request.getUsefulLifeMonths());
        asset.setMaintenanceSchedule(request.getMaintenanceSchedule());
        asset.setLastMaintenanceDate(request.getLastMaintenanceDate());
        asset.setNextMaintenanceDate(request.getNextMaintenanceDate());
        if (request.getStatus() != null && !request.getStatus().isBlank()) asset.setStatus(request.getStatus().trim());
        asset.setNotes(request.getNotes());
        asset.setUpdatedAt(LocalDateTime.now());

        return assetRepository.save(asset);
    }

    @Override
    @Transactional(readOnly = true)
    public PhysicalAsset getById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activo físico no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhysicalAsset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public void deleteAsset(Long id) {
        if (!assetRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Activo físico no encontrado con ID: " + id);
        }
        assetRepository.deleteById(id);
    }
}
