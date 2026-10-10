package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.PhysicalAsset;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.AssetResponse;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.PhysicalAssetEntity;
import org.springframework.stereotype.Component;

@Component
public class PhysicalAssetMapper {

    public PhysicalAsset toDomain(PhysicalAssetEntity entity) {
        if (entity == null) return null;
        return PhysicalAsset.builder()
                .id(entity.getId())
                .assetCode(entity.getAssetCode())
                .name(entity.getName())
                .category(entity.getCategory())
                .location(entity.getLocation())
                .purchasePrice(entity.getPurchasePrice())
                .purchaseDate(entity.getPurchaseDate())
                .usefulLifeMonths(entity.getUsefulLifeMonths())
                .depreciationAccumulated(entity.getDepreciationAccumulated())
                .maintenanceSchedule(entity.getMaintenanceSchedule())
                .lastMaintenanceDate(entity.getLastMaintenanceDate())
                .nextMaintenanceDate(entity.getNextMaintenanceDate())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public PhysicalAssetEntity toEntity(PhysicalAsset domain) {
        if (domain == null) return null;
        return PhysicalAssetEntity.builder()
                .id(domain.getId())
                .assetCode(domain.getAssetCode())
                .name(domain.getName())
                .category(domain.getCategory())
                .location(domain.getLocation())
                .purchasePrice(domain.getPurchasePrice())
                .purchaseDate(domain.getPurchaseDate())
                .usefulLifeMonths(domain.getUsefulLifeMonths())
                .depreciationAccumulated(domain.getDepreciationAccumulated())
                .maintenanceSchedule(domain.getMaintenanceSchedule())
                .lastMaintenanceDate(domain.getLastMaintenanceDate())
                .nextMaintenanceDate(domain.getNextMaintenanceDate())
                .status(domain.getStatus())
                .notes(domain.getNotes())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public AssetResponse toResponse(PhysicalAsset domain) {
        if (domain == null) return null;
        return AssetResponse.builder()
                .id(domain.getId())
                .assetCode(domain.getAssetCode())
                .name(domain.getName())
                .category(domain.getCategory())
                .location(domain.getLocation())
                .purchasePrice(domain.getPurchasePrice())
                .purchaseDate(domain.getPurchaseDate())
                .usefulLifeMonths(domain.getUsefulLifeMonths())
                .depreciationAccumulated(domain.getDepreciationAccumulated())
                .maintenanceSchedule(domain.getMaintenanceSchedule())
                .lastMaintenanceDate(domain.getLastMaintenanceDate())
                .nextMaintenanceDate(domain.getNextMaintenanceDate())
                .status(domain.getStatus())
                .notes(domain.getNotes())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
