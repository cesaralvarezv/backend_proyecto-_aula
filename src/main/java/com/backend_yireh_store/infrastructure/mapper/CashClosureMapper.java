package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.CashClosure;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CashClosureResponse;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CashClosureEntity;
import org.springframework.stereotype.Component;

@Component
public class CashClosureMapper {

    public CashClosure toDomain(CashClosureEntity entity) {
        if (entity == null) return null;
        return CashClosure.builder()
                .id(entity.getId())
                .cashierId(entity.getCashierId())
                .cashierEmail(entity.getCashierEmail())
                .openingAmount(entity.getOpeningAmount())
                .openingTime(entity.getOpeningTime())
                .closingTime(entity.getClosingTime())
                .cashSalesSystem(entity.getCashSalesSystem())
                .virtualWalletSales(entity.getVirtualWalletSales())
                .cardSales(entity.getCardSales())
                .totalSalesSystem(entity.getTotalSalesSystem())
                .declaredCash(entity.getDeclaredCash())
                .discrepancy(entity.getDiscrepancy())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public CashClosureEntity toEntity(CashClosure domain) {
        if (domain == null) return null;
        return CashClosureEntity.builder()
                .id(domain.getId())
                .cashierId(domain.getCashierId())
                .cashierEmail(domain.getCashierEmail())
                .openingAmount(domain.getOpeningAmount())
                .openingTime(domain.getOpeningTime())
                .closingTime(domain.getClosingTime())
                .cashSalesSystem(domain.getCashSalesSystem())
                .virtualWalletSales(domain.getVirtualWalletSales())
                .cardSales(domain.getCardSales())
                .totalSalesSystem(domain.getTotalSalesSystem())
                .declaredCash(domain.getDeclaredCash())
                .discrepancy(domain.getDiscrepancy())
                .status(domain.getStatus())
                .notes(domain.getNotes())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    public CashClosureResponse toResponse(CashClosure domain) {
        if (domain == null) return null;
        return CashClosureResponse.builder()
                .id(domain.getId())
                .cashierId(domain.getCashierId())
                .cashierEmail(domain.getCashierEmail())
                .openingAmount(domain.getOpeningAmount())
                .openingTime(domain.getOpeningTime())
                .closingTime(domain.getClosingTime())
                .cashSalesSystem(domain.getCashSalesSystem())
                .virtualWalletSales(domain.getVirtualWalletSales())
                .cardSales(domain.getCardSales())
                .totalSalesSystem(domain.getTotalSalesSystem())
                .declaredCash(domain.getDeclaredCash())
                .discrepancy(domain.getDiscrepancy())
                .status(domain.getStatus() != null ? domain.getStatus().name() : null)
                .notes(domain.getNotes())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
